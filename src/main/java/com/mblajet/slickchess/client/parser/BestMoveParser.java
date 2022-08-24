package com.mblajet.slickchess.client.parser;

import com.mblajet.slickchess.client.model.BestMove;
import java.util.regex.Matcher;

public class BestMoveParser extends AbstractParser<BestMove> {

    private static final String BEST_MOVE_REGEX = "bestmove\\s([\\d\\w]*)\\sponder\\s([\\d\\w]*)";

    private BestMoveParser(String regex) {
        super(regex);
    }

    public BestMoveParser() {
        this(BEST_MOVE_REGEX);
    }

    @Override
    protected BestMove doParse(String line, Matcher matcher) {
        var curr = matcher.group(1);
        var ponder = matcher.group(2);
        return new BestMove(curr, ponder);
    }
}
