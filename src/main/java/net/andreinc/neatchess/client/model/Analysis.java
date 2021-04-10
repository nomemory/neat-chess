package net.andreinc.neatchess.client.model;

import java.util.Map;

public class Analysis {

    private final Map<Integer, Move> moves;

    public Analysis(Map<Integer, Move> moves) {
        this.moves = moves;
    }

    public Map<Integer, Move> getAllMoves() {
        return moves;
    }

    public int getTotalLines() {
        return moves.size();
    }

    public Move getBestMove() {
        return moves.get(1);
    }

    @Override
    public String toString() {
        return "Analysis{" +
                "moves=" + moves +
                '}';
    }
}
