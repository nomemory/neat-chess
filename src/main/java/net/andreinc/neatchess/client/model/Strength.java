package net.andreinc.neatchess.client.model;

public class Strength implements Comparable<Strength> {

    private double score = Double.MAX_VALUE;
    private boolean isMate = false;
    private int mateIn = Integer.MIN_VALUE;

    public Strength(String str) {
        if (str.startsWith("cp")) {
            this.score = Double.parseDouble(str.split(" ")[1])/100;
        } else {
            this.isMate = true;
            this.mateIn = Integer.parseInt(str.split(" ")[1]);
        }
    }

    public Double getScore() {
        return this.score;
    }

    public int getMateIn() {
        return this.mateIn;
    }

    public boolean isForcedMate() {
        return this.isMate;
    }

    @Override
    public String toString() {
        if (isMate) {
            return "M" + mateIn;
        }
        return score+"";
    }

    @Override
    public int compareTo(Strength o) {
        if (this.isMate && o.isMate) {
            return Integer.compare(this.mateIn, o.mateIn);
        } else if (this.isMate && !o.isMate) {
            return 1;
        } else if (!this.isMate && o.isMate) {
            return -1;
        }
        return Double.compare(this.score, o.score);
    }
}
