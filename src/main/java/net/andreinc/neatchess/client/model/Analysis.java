package net.andreinc.neatchess.client.model;

import java.util.Map;

public class Analysis {

    private final Map<Integer, Move> moves;

    public boolean isMate = false;

    public boolean isDraw = false;

    public Analysis(Map<Integer, Move> moves) {
        this.moves = moves;
    }

    public Analysis(Map<Integer, Move> moves, boolean isMate, boolean isDraw) {
        this(moves);
        this.isMate = isMate;
        this.isDraw= isDraw;
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

    public boolean isMate() {
        return isMate;
    }

    public boolean isDraw() {
        return isDraw;
    }

    @Override
    public String toString() {
        return "Analysis{" +
                "moves=" + moves +
                ", isMate=" + isMate +
                ", isDraw=" + isDraw +
                '}';
    }
}
