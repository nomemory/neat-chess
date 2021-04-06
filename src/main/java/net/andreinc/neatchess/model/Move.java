package net.andreinc.neatchess.model;

import java.util.Arrays;

public class Move {

    private final String lan;
    private final double score;
    private final int pv;
    private String[] continuation;

    public Move(String lan, double score, int pv, String continuation[]) {
        this.lan = lan;
        this.score = score;
        this.pv = pv;
        this.continuation = continuation;
    }

    public String getLan() {
        return lan;
    }

    @Override
    public String toString() {
        return "Move{" +
                "lan='" + lan + '\'' +
                ", score=" + score +
                ", pv=" + pv +
                ", continuation=" + Arrays.toString(continuation) +
                '}';
    }
}
