package net.andreinc.neatchess.client.model;

import java.util.Objects;

public class BestMove {

    public String current;
    public String ponder;

    public BestMove(String current, String ponder) {
        this.current = current;
        this.ponder = ponder;
    }

    public String getCurrent() {
        return current;
    }

    public String getPonder() {
        return ponder;
    }

    @Override
    public String toString() {
        return "BestMove{" +
                "current=" + current +
                ", ponder=" + ponder +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestMove bestMove = (BestMove) o;
        return Objects.equals(current, bestMove.current) && Objects.equals(ponder, bestMove.ponder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, ponder);
    }
}
