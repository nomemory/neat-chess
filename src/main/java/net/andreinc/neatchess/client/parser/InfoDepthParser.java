package net.andreinc.neatchess.client.parser;

import net.andreinc.neatchess.client.model.Move;
import net.andreinc.neatchess.client.model.Strength;

import java.util.regex.Matcher;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class InfoDepthParser extends AbstractParser<Move> {

    public static final String INFO_DEPTH_REGEX = "info depth ([\\w]*) seldepth [\\w]* multipv ([\\w]*) score (cp ([\\-\\w]*)|mate ([\\w*])) [\\s\\w]*pv ([\\w]*)\\s*([\\s\\w]*)";

    private InfoDepthParser(String regex) {
        super(regex);
    }

    public InfoDepthParser() {
        this(INFO_DEPTH_REGEX);
    }

    @Override
    protected Move doParse(String line, Matcher matcher) {
        // group(1) => depth
        // group(2) => multipv
        // group(3) => cp <score> || mate <num>
        // group(4) => <score>
        // group(5) => <num>
        // group(6) => move
        // group(7) => continuation
        var depth = parseInt(matcher.group(1));
        var multipv = parseInt(matcher.group(2));
        var strength = matcher.group(3);
        var move = matcher.group(6);
        var continuation = matcher.group(7).split(" ");

        return new Move(move, depth, new Strength(strength), multipv, continuation);
    }
}
