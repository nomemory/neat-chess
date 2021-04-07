package net.andreinc.neatchess.model;

import java.util.Arrays;

public class Move implements Comparable<Move> {

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

    public double getScore() {
        return score;
    }

    public Integer  getPv() {
        return pv;
    }

    public String[] getContinuation() {
        return continuation;
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


    @Override
    public int compareTo(Move o) {
        return this.pv - o.pv;
    }
}
