package net.andreinc.neatchess.parser;

import net.andreinc.neatchess.model.Move;
import java.util.regex.Matcher;

public class InfoDepthParser extends AbstractParser<Move> {

    public static final String INFO_DEPTH_REGEX = "info depth [\\w]* seldepth [\\w]*\\smultipv ([\\w]*) score cp ([\\-\\w]*)[\\s\\w]*pv ([\\w]*)\\s*([\\s\\w]*)";

    private InfoDepthParser(String regex) {
        super(regex);
    }

    public InfoDepthParser() {
        this(INFO_DEPTH_REGEX);
    }

    @Override
    protected Move doParse(String line, Matcher matcher) {
        var pv = matcher.group(1);
        var score = matcher.group(2);
        var move = matcher.group(3);
        var continuation = matcher.group(4);
        return new Move(move, Double.parseDouble(score), Integer.parseInt(pv), continuation.split(" "));
    }
}
