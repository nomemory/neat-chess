package net.andreinc.neatchess.parser;

import java.util.regex.Pattern;

public class EngineNameParser {

    private static class EngineNameParserHolder {
        public static final EngineNameParser instance = new EngineNameParser();
    }

    public static EngineNameParser getInstance() {
        return EngineNameParserHolder.instance;
    }

    private static String NAME_REGEX = "(id name) ([\\w].*)";
    private static Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    public String parse(String line) {
        var matcher = NAME_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid name line: " + line);
        }
        return matcher.group(2);
    }
}
