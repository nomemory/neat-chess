package net.andreinc.neatchess;

import net.andreinc.neatchess.model.option.EngineOption;
import net.andreinc.neatchess.model.option.SpinEngineOption;
import net.andreinc.neatchess.parser.EngineNameParser;
import net.andreinc.neatchess.parser.EngineOptionParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class UCI {

    private static final String STOCKFISH = "stockfish";

    private Process process;
    private BufferedReader reader;
    private OutputStreamWriter writer;
    private Map<String, EngineOption> options;
    private String engineName;

    private EngineOptionParser engineOptionParser = EngineOptionParser.getInstance();
    private EngineNameParser engineNameParser = EngineNameParser.getInstance();

    public void start(String cmd) {
        var pb = new ProcessBuilder(cmd);
        try {
            this.process = pb.start();
            this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            this.writer = new OutputStreamWriter(process.getOutputStream());
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

    public void command(String cmd, long timeout, Predicate<String> breakCondition, Consumer<String> lineConsumer) throws InterruptedException, ExecutionException, TimeoutException {
        runAsync(() -> {
            try {
                writer.flush();
                writer.write(cmd + "\n");
                writer.write("isready\n");
                writer.flush();
                String line;
                while (true) {
                    line = reader.readLine();
                    if (breakCondition.test(line)) {
                        break;
                    }
                    if (null != lineConsumer) {
                        lineConsumer.accept(line);
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }).get(timeout, TimeUnit.MILLISECONDS);
    }

    public void command(String cmd, Predicate<String> breakCondition, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        command(cmd, timeout, breakCondition, null);
    }

    public void command(String cmd, long timeout) throws TimeoutException, ExecutionException, InterruptedException {
        command(cmd, timeout, s -> s.equals("readyok"), null);
    }

    public void uci(long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        final List<String> rawOptions = new ArrayList<>();
        command("uci", timeout, (s) -> "uciok".equals(s), (line) -> {
            if (line.startsWith("option name")) {
                rawOptions.add(line);
            }
            else if (line.startsWith("id name")) {
                this.engineName = engineNameParser.parse(line);
            }
        });
        this.options = rawOptions.stream().map(engineOptionParser::parse)
                                    .collect(toMap(EngineOption::getName, identity()));
    }

    public void ucinewgame(long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        command("ucinewgame", timeout);
    }

    public void setoption(String optionName, String value, long timeout) throws TimeoutException, ExecutionException, InterruptedException {
        command(format("setoption name %s value %s", optionName, value), timeout);
    }

    public void setoption(String optionName, Integer value, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        command(format("setoption name %s value %d", optionName, value), timeout);
    }

    public void positionfen(String fen, long timeout) throws TimeoutException, ExecutionException, InterruptedException {
        command("ucinewgame", timeout);
        command(format("position fen %", fen), timeout);
    }

    public Map<Double, String> go(int multiPv, long moveTime, long timeOut) throws TimeoutException, ExecutionException, InterruptedException {
        command(format("go movetime %d", moveTime),  timeOut, (s) -> s.startsWith("bestmove"), (line) -> {
            if (line.startsWith("info depth")) {
                System.out.println(line);
            }
        });
        return new TreeMap<>();
    }

    public static void main(String[] args) {
        long timeout = 4000l;
        var uci = new UCI();
        uci.start(STOCKFISH);
        try {
            uci.uci(3000l);
            uci.setoption("MultiPV", 10, timeout);
            uci.go(10,1000l, timeout);
            uci.go(20, 1000l, timeout);
            uci.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        uci.close();
    }

}
