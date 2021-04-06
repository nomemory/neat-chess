package net.andreinc.neatchess.parser;

import net.andreinc.neatchess.exception.UCIParsingExcpetion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractParser<T> {
    protected final String regex;
    protected final Pattern pattern;

    public AbstractParser(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    protected abstract T doParse(String line, Matcher matcher);

    public T parse(String line) {
        var matcher = pattern.matcher(line);
        if (matcher.matches()) {
            return doParse(line, matcher);
        }
        throw new UCIParsingExcpetion(line);
    }
}
