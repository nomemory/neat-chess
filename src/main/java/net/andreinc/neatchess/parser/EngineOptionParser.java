package net.andreinc.neatchess.parser;

import net.andreinc.neatchess.model.option.*;

import java.util.regex.Pattern;

public class EngineOptionParser {

    private static class EngineOptionParserHolder {
        public static final EngineOptionParser instance = new EngineOptionParser();
    }

    private EngineOptionParser() {}

    public static EngineOptionParser getInstance() {
        return EngineOptionParserHolder.instance;
    }

    protected String OPTION_REGEX = "option name ([\\w\\s]+) type (button|check|combo|spin|string)\\s*(default\\s)*([\\w\\<\\>\\-\\.]*)\\s*([\\w\\s-]*)";
    protected String SPIN_REGEX = "min ([\\-\\d]*) max ([\\d]*)";
    protected String line = "(var)\\s([\\w+]+)";

    protected Pattern OPTION_PATTERN = Pattern.compile(OPTION_REGEX);
    protected Pattern SPIN_PATTERN = Pattern.compile(SPIN_REGEX);
    protected Pattern COMBO_PATTERN = Pattern.compile(line);

    public EngineOption parse(String line) {
        var matcher = OPTION_PATTERN.matcher(line);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Cannot parse line: " + line);
        }

        var optionName = matcher.group(1);
        var optionType = matcher.group(2);
        var defaultValue = matcher.group(4);
        var payload  = matcher.group(5);

        switch (optionType) {
            case "button" : return new ButtonEngineOption(optionName, defaultValue);
            case "check" : return new CheckEngineOption(optionName, Boolean.parseBoolean(defaultValue));
            case "combo" : return new ComboEngineOption(optionName, defaultValue);
            case "spin" : {
                var mmMatch = SPIN_PATTERN.matcher(payload);
                if (!mmMatch.matches()) {
                    throw new IllegalArgumentException("Invalid spin option:" + line);
                }
                var min = Integer.parseInt(mmMatch.group(1));
                var max = Integer.parseInt(mmMatch.group(2));
                return new SpinEngineOption(optionName, Integer.parseInt(defaultValue), min, max);
            }
            case "string" : return new StringEngineOption(optionName, defaultValue);
            default: throw new IllegalArgumentException("Invalid option type: " + line);
        }
    }
}
