package net.andreinc.neatchess.model;

import java.util.Map;
import java.util.TreeMap;

public class Analysis {
    private BestMove bestMove;
    private Map<Integer, Move> moves = new TreeMap<>();

    public Analysis(BestMove bestMove, Map<Integer, Move> moves) {
        this.bestMove = bestMove;
        this.moves = moves;
    }

    public BestMove getBestMove() {
        return bestMove;
    }

    public Map<Integer, Move> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return "Analysis{" +
                "bestMove=" + bestMove +
                ", moves=" + moves +
                '}';
    }
}
