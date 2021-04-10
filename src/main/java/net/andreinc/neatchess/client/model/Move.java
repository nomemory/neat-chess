package net.andreinc.neatchess.client.model;

import java.util.Arrays;

public class Move implements Comparable<Move> {

    private final String lan;
    private final Strength strength;
    private final int pv;
    private final int depth;
    private String[] continuation;

    public Move(String lan, int depth, Strength strength, int pv, String continuation[]) {
        this.lan = lan;
        this.depth = depth;
        this.strength = strength;
        this.pv = pv;
        this.continuation = continuation;
    }

    public String getLan() {
        return lan;
    }

    public Strength getStrength() {
        return strength;
    }

    public Integer  getPv() {
        return pv;
    }

    public String[] getContinuation() {
        return continuation;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return "Move{" +
                "lan='" + lan + '\'' +
                ", strength=" + strength +
                ", pv=" + pv +
                ", depth=" + depth +
                ", continuation=" + Arrays.toString(continuation) +
                '}';
    }


    @Override
    public int compareTo(Move o) {
        return this.pv - o.pv;
    }
}
