package net.andreinc.neatchess.client;

import net.andreinc.neatchess.client.processor.AnalysisProcessor;
import net.andreinc.neatchess.client.processor.BestMoveProcessor;
import net.andreinc.neatchess.client.processor.EngineInfoProcessor;
import net.andreinc.neatchess.client.exception.*;
import net.andreinc.neatchess.client.model.Analysis;
import net.andreinc.neatchess.client.model.BestMove;
import net.andreinc.neatchess.client.model.EngineInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.function.Function.identity;
import static net.andreinc.neatchess.client.breaks.Break.breakOn;

/**
 * <p>This class represents a simple UCI Client implementation.</p>
 *
 * <p>Not all the UCI operations are implemented, but the most important ones are.</p>
 *
 * <p>The client was tested with Stockfish 13</p>
 */
public class UCI {

    private static final String STOCKFISH = "stockfish";
    private static final String LC0 = "lc0";

    private static final long DEFAULT_TIMEOUT_VALUE = 60_000l;

    // Processors
    public static final BestMoveProcessor bestMove = new BestMoveProcessor();
    public static final AnalysisProcessor analysis = new AnalysisProcessor();
    public static final EngineInfoProcessor engineInfo = new EngineInfoProcessor();

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

    public void startStockfish() {
        start(STOCKFISH);
    }

    public void startLc0() {
        start(LC0);
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
            throw new UCIRuntimeException(e);
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
        CompletableFuture<List<String>> command = supplyAsync(() -> {
            final List<String> output = new ArrayList<>();
            try {
                writer.flush();
                writer.write(cmd + "\n");
                writer.write("isready\n");
                writer.flush();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Unknown command")) {
                        throw new UCIUnknownCommandException(line);
                    }
                    if (line.contains("Unexpected token")) {
                        throw new UCIUnknownCommandException("Unexpected token: " + line);
                    }
                    output.add(line);
                    if (breakCondition.test(line)) {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new UCIUncheckedIOException(e);
            } catch (RuntimeException e) {
                throw new UCIRuntimeException(e);
            }
            return output;
        });

        CompletableFuture<UCIResponse<T>> processorFuture = command.handle((list, ex) -> {
            try {
                var result = commandProcessor.apply(list);
                return new UCIResponse<>(result, (UCIRuntimeException) ex);
            } catch (RuntimeException e) {
                return new UCIResponse<T>(null, new UCIRuntimeException(e));
            }
        });

        try {
            return processorFuture.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return new UCIResponse<>(null, new UCITimeoutException(e));
        } catch (RuntimeException e ) {
            return new UCIResponse<>(null, new UCIRuntimeException(e));
        } catch (InterruptedException e) {
           return new UCIResponse<>(null, new UCIInterruptedException(e));
        } catch (ExecutionException e) {
           return new UCIResponse<>(null, new UCIExecutionException(e));
        }

    }

    public <T> UCIResponse<T> command(String cmd, Function<List<String>, T> commandProcessor, Predicate<String> breakCondition) {
        return command(cmd, commandProcessor, breakCondition, defaultTimeout);
    }

    public UCIResponse<EngineInfo> getEngineInfo() {
        return command("uci", engineInfo::process, breakOn("readyok"), defaultTimeout);
    }

    public UCIResponse<List<String>> uciNewGame(long timeout) {
        return command("ucinewgame", identity(), breakOn("readyok"), timeout);
    }

    public UCIResponse<List<String>> uciNewGame() {
        return command("ucinewgame", identity(), breakOn("readyok"), defaultTimeout);
    }

    public UCIResponse<List<String>> setOption(String optionName, String value, long timeout) {
        return command(format("setoption name %s value %s", optionName, value), identity(), breakOn("readyok"), timeout);
    }

    public UCIResponse<List<String>> setOption(String optionName, String value) {
        return setOption(optionName, value, defaultTimeout);
    }

    public UCIResponse<List<String>> positionFen(String fen, long timeout) {
        return command(format("position fen %s", fen), identity(), breakOn("readyok"), timeout);
    }

    public UCIResponse<List<String>> positionFen(String fen) {
        return positionFen(fen, defaultTimeout);
    }

    public UCIResponse<BestMove> bestMove(int depth, long timeout) {
        return command(format("go bestmove depth %d", depth), bestMove::process, breakOn("bestmove"), timeout);
    }

    public UCIResponse<BestMove> bestMove(int depth) {
        return bestMove(depth, defaultTimeout);
    }

    public UCIResponse<BestMove> bestMove(long moveTime, long timeout) {
        return command(format("go bestmove movetime %d", moveTime), bestMove::process, breakOn("bestmove"), timeout);
    }

    public UCIResponse<BestMove> bestMove(long moveTime) {
        return bestMove(moveTime, defaultTimeout);
    }

    public UCIResponse<Analysis> analysis(long moveTime, long timeout) {
        return command(format("go movetime %d", moveTime), analysis::process, breakOn("bestmove"), timeout);
    }

    public UCIResponse<Analysis> analysis(long moveTime) {
        return analysis(moveTime, defaultTimeout);
    }

    public UCIResponse<Analysis> analysis(int depth, long timeout) {
        return command(format("go depth %d", depth), analysis::process, breakOn("bestmove"), timeout);
    }

    public UCIResponse<Analysis> analysis(int depth) {
        return analysis(depth, defaultTimeout);
    }


}