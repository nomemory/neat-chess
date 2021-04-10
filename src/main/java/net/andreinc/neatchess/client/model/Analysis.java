package net.andreinc.neatchess.client.model;

import java.util.Map;

public class Analysis {

    private final Map<Integer, Move> moves;

    public boolean isMate = false;

    public Analysis(Map<Integer, Move> moves) {
        this.moves = moves;
    }

    public Analysis(Map<Integer, Move> moves, boolean isMate) {
        this(moves);
        this.isMate = true;
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
                ", isMate=" + isMate +
                '}';
    }
}
