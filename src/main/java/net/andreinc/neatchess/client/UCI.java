package net.andreinc.neatchess.client;

import net.andreinc.neatchess.client.processor.AnalysisProcessor;
import net.andreinc.neatchess.client.processor.BestMoveProcessor;
import net.andreinc.neatchess.client.processor.EngineInfoProcessor;
import net.andreinc.neatchess.exception.UCITimeoutException;
import net.andreinc.neatchess.exception.UCIUnknownCommandException;
import net.andreinc.neatchess.model.Analysis;
import net.andreinc.neatchess.model.BestMove;
import net.andreinc.neatchess.model.EngineInfo;
import net.andreinc.neatchess.parser.BestMoveParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.function.Function.identity;

public class UCI {

    private static final String STOCKFISH = "stockfish";
    private static final long DEFAULT_TIMEOUT_VALUE = 60_000l;

    // Processors
    public static final BestMoveProcessor bestMoveProcessor = new BestMoveProcessor();
    public static final AnalysisProcessor analysisProcessor = new AnalysisProcessor();
    public static final EngineInfoProcessor engineInfoProcessor = new EngineInfoProcessor();

    // Breaks
    public static final Predicate<String> BEST_MOVE_BREAK = s -> s.startsWith("bestmove");
    public static final Predicate<String> READY_OK_BREAK = s->s.startsWith("readyok");
    public static final Predicate<String> UCI_OK_BREAK = s -> s.startsWith("uciok");

    private final long defaultTimeout;

    private Process process = null;
    private BufferedReader reader = null;
    private OutputStreamWriter writer = null;

    public UCI(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public UCI() {
        this(DEFAULT_TIMEOUT_VALUE);
    }

    public void start(String cmd) {
        var pb = new ProcessBuilder(cmd);
        try {
            this.process = pb.start();
            this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            this.writer = new OutputStreamWriter(process.getOutputStream());
            // An UCI command needs to be sent to initialize the engine
            getEngineInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (this.process.isAlive()) {
            this.process.destroy();
        }
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public <T> UCIResponse<T> command(String cmd, Function<List<String>, T> commandProcessor, Predicate<String> breakCondition, long timeout)  {
        CompletableFuture<List<String>> processFuture = supplyAsync(() -> {
            final List<String> output = new ArrayList<>();
            try {
                writer.flush();
                writer.write(cmd + "\n");
                writer.write("isready\n");
                writer.flush();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Unknown command")) {
                        throw new UCIUnknownCommandException(line);
                    }
                    output.add(line);
                    if (breakCondition.test(line)) {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return output;
        });

        CompletableFuture<UCIResponse<T>> responseFuture = processFuture.handle((list, ex) -> {
            var result = commandProcessor.apply(list);
            return new UCIResponse<>(result, ex);
        });

        UCIResponse<T> response = null;
        try {
            response = responseFuture.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            response.setException(new UCITimeoutException(e));
        } finally {
            return response;
        }

    }

    public <T> UCIResponse<T> command(String cmd, Function<List<String>, T> commandProcessor, Predicate<String> breakCondition) {
        return command(cmd, commandProcessor, breakCondition, defaultTimeout);
    }

    public UCIResponse<EngineInfo> getEngineInfo() {
        return command("uci", engineInfoProcessor::process, UCI_OK_BREAK, defaultTimeout);
    }

    public UCIResponse<List<String>> uciNewGame(long timeout) {
        return command("ucinewgame", identity(), READY_OK_BREAK, timeout);
    }

    public UCIResponse<List<String>> uciNewGame() {
        return command("ucinewgame", identity(), READY_OK_BREAK, defaultTimeout);
    }

    public UCIResponse<List<String>> setOption(String optionName, String value, long timeout) {
        return command(format("setoption name %s value %s", optionName, value), identity(), READY_OK_BREAK, timeout);
    }

    public UCIResponse<List<String>> setOption(String optionName, String value) {
        return setOption(optionName, value, defaultTimeout);
    }

    public UCIResponse<List<String>> positionFen(String fen, long timeout) {
        return command(format("position fen %", fen), identity(), READY_OK_BREAK, timeout);
    }

    public UCIResponse<List<String>> positionFen(String fen) {
        return positionFen(fen, defaultTimeout);
    }

    public UCIResponse<BestMove> bestMove(int depth, long timeout) {
        return command(format("go bestmove depth %d", depth), new BestMoveProcessor()::process, BEST_MOVE_BREAK, timeout);
    }

    public UCIResponse<BestMove> bestMove(int depth) {
        return bestMove(depth, defaultTimeout);
    }

    public UCIResponse<BestMove> bestMove(long moveTime, long timeout) {
        return command(format("go bestmove movetime %d", moveTime), bestMoveProcessor::process, BEST_MOVE_BREAK, timeout);
    }

    public UCIResponse<BestMove> bestMove(long moveTime) {
        return bestMove(moveTime, defaultTimeout);
    }

    public UCIResponse<Analysis> analysis(long moveTime, long timeout) {
        return null;
    }

    public UCIResponse<Analysis> analysis(long moveTime) {
        return analysis(moveTime, defaultTimeout);
    }

    public UCIResponse<Analysis> analysis(int depth, long timeout) {
        return null;
    }

    public UCIResponse<Analysis> analysis(int depth) {
        return analysis(depth, defaultTimeout);
    }

    public static void main(String[] args) {
        var uci = new UCI();
        uci.start(STOCKFISH);
        uci.setOption("MultiPV", "10");
        System.out.println(uci.bestMove(24, 60000l));
    }
}
