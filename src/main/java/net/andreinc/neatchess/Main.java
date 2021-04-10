package net.andreinc.neatchess;

import net.andreinc.neatchess.client.UCI;
import net.andreinc.neatchess.client.UCIResponse;
import net.andreinc.neatchess.client.model.Analysis;

public class Main {
    public static void main(String[] args) {
        var uci = new UCI();
        uci.startStockfish();
        uci.setOption("MultiPV", "10");

        uci.uciNewGame();
        uci.positionFen("r1bqkb1r/2pp1ppp/p1n2n2/1p2p3/4P3/1B3N2/PPPP1PPP/RNBQK2R w KQkq - 2 6");
        UCIResponse<Analysis> response = uci.analysis(18);
        var analysis = response.getResultOrThrow();


        System.out.println("Is Draw: " + analysis.isDraw());
        System.out.println("Is Mate: " + analysis.isMate());
        System.out.println("Best move: \n\t" + analysis.getBestMove());

        System.out.println("Best moves:");
        var moves = analysis.getAllMoves();
        moves.forEach((idx, move) -> {
            System.out.println("\t" + move);
        });

        uci.close();
    }
}
