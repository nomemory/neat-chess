package net.andreinc.neatchess;

import net.andreinc.neatchess.client.UCI;
import net.andreinc.neatchess.client.UCIResponse;
import net.andreinc.neatchess.client.exception.UCIRuntimeException;
import net.andreinc.neatchess.client.exception.UCITimeoutException;
import net.andreinc.neatchess.client.model.Analysis;
import net.andreinc.neatchess.client.model.BestMove;
import net.andreinc.neatchess.client.model.EngineInfo;
import net.andreinc.neatchess.client.model.option.EngineOption;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static net.andreinc.neatchess.client.breaks.Break.breakOn;

public class Main {
    public static void main(String[] args) {
        var uci = new UCI(5000l); // default timeout 5 seconds
        uci.startStockfish();

        // Setting the MultiPV to 10 so that engine returns the 10 best lines
        uci.setOption("MultiPV", "10", 3000l).getResultOrThrow();

        // "Clear" the previous engine state
        uci.uciNewGame();

        // Setting the position
        uci.positionFen("rnbqk3/pp6/5b2/2pp1p1p/1P3P1P/5N2/P1PPP1P1/RNBQKB2 b Qq - 0 14");

        // Retrieving the best move after analysing the game with a depth of
        uci.close();

//        UCIResponse<EngineInfo> response = uci.getEngineInfo();
//        if (response.success()) {
//            // Engine name
//            EngineInfo engineInfo = response.getResult();
//            System.out.println("Engine name:" + engineInfo.getName());
//            // Supported engine options
//            System.out.println("Supported engine options:");
//            Map<String, EngineOption> engineOptions = engineInfo.getOptions();
//            engineOptions.forEach((key, value) -> {
//                System.out.println("\t" + key);
//                System.out.println("\t\t" + value);
//            });
//        }
//        uci.close();

//        uci.setOption("MultiPv", "5");
        uci.positionFen("rnbqk3/pp6/5b2/2pp1p1p/1P3P1P/5N2/P1PPP1P1/RNBQKB2 b Qq - 0 14");
//        uci.positionFen("3b1q1q/1N2PRQ1/rR3KBr/B4PP1/2Pk1r1b/1P2P1N1/2P2P2/8 b - -");
//        //uci.positionFen("1k6/1P6/2K5/1N6/8/4B3/8/8 b - - 0 95");
//        UCIResponse<Analysis> response = uci.analysis(10);
//        System.out.println(response);
//        Analysis analysis = response.getResultOrThrow();
//        analysis.getAllMoves().entrySet().forEach(System.out::println);
//        uci.close();
    }
}
