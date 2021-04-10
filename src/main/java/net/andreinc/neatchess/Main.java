package net.andreinc.neatchess;

import net.andreinc.neatchess.client.UCI;
import net.andreinc.neatchess.client.UCIResponse;
import net.andreinc.neatchess.client.model.Analysis;

public class Main {
    public static void main(String[] args) {
        var uci = new UCI();
        uci.startStockfish();
        uci.setOption("MultiPv", "5");
        uci.positionFen("rnbqk3/pp6/5b2/2pp1p1p/1P3P1P/5N2/P1PPP1P1/RNBQKB2 b Qq - 0 14");
        UCIResponse<Analysis> response = uci.analysis(10);
        System.out.println(response);
        Analysis analysis = response.getResultOrThrow();
        analysis.getAllMoves().entrySet().forEach(System.out::println);
        uci.close();
    }
}
