package net.andreinc.neatchess.client.parser;

import java.util.regex.Matcher;

public class EngineNameParser extends AbstractParser<String> {

    private static String NAME_REGEX = "(id name) ([\\w].*)";

    private EngineNameParser(String regex) {
        super(regex);
    }

    public EngineNameParser() {
        this(NAME_REGEX);
    }

    @Override
    protected String doParse(String line, Matcher matcher) {
        return matcher.group(2);
    }
}
