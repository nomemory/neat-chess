package net.andreinc.neatchess.client.model;

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
}
