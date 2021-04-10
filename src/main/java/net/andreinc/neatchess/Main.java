package net.andreinc.neatchess;

import net.andreinc.neatchess.client.UCI;
import net.andreinc.neatchess.client.UCIResponse;
import net.andreinc.neatchess.client.model.Analysis;

public class Main {
    public static void main(String[] args) {
        var uci = new UCI();
        uci.startStockfish();
        uci.setOption("MultiPv", "5");
        //uci.positionFen("rnbqk3/pp6/5b2/2pp1p1p/1P3P1P/5N2/P1PPP1P1/RNBQKB2 b Qq - 0 14");
        uci.positionFen("3b1q1q/1N2PRQ1/rR3KBr/B4PP1/2Pk1r1b/1P2P1N1/2P2P2/8 b - -");
        UCIResponse<Analysis> response = uci.analysis(10);
        System.out.println(response);
        Analysis analysis = response.getResultOrThrow();
        analysis.getAllMoves().entrySet().forEach(System.out::println);
        uci.close();
    }
}
