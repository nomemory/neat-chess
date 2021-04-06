package net.andreinc.neatchess.model;

import java.util.Map;
import java.util.TreeMap;

public class Analysis {
    public BestMove bestMove;
    private Map<Double, Move> moves = new TreeMap<>();
}
