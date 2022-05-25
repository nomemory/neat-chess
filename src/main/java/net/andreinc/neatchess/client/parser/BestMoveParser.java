package net.andreinc.neatchess.client.parser;

import net.andreinc.neatchess.client.model.BestMove;
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
        String curr = matcher.group(1);
        String ponder = matcher.group(2);
        return new BestMove(curr, ponder);
    }
}
