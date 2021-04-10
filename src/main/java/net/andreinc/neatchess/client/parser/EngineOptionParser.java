package net.andreinc.neatchess.client.parser;

import net.andreinc.neatchess.client.model.option.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EngineOptionParser extends AbstractParser<EngineOption> {

    private static String OPTION_REGEX = "option name ([\\w\\s]+) type (button|check|combo|spin|string)\\s*(default\\s)*([\\w\\<\\>\\-\\.]*)\\s*([\\w\\s-]*)";
    private static String SPIN_REGEX = "min ([\\-\\d]*) max ([\\d]*)";
    private static String COMBO_REGEX = "(var)\\s([\\w+]+)";

    private Pattern spinPattern;
    private Pattern comboPattern;

    private EngineOptionParser(String regex) {
        super(regex);
        this.spinPattern = Pattern.compile(SPIN_REGEX);
        this.comboPattern = Pattern.compile(COMBO_REGEX);
    }

    public EngineOptionParser() {
        this(OPTION_REGEX);
    }

    @Override
    protected EngineOption doParse(String line, Matcher matcher) {
        var optionName = matcher.group(1);
        var optionType = matcher.group(2);
        var defaultValue = matcher.group(4);
        var payload  = matcher.group(5);
        switch (optionType) {
            case "button" : return new ButtonEngineOption(optionName, defaultValue);
            case "check" : return new CheckEngineOption(optionName, Boolean.parseBoolean(defaultValue));
            case "combo" : return new ComboEngineOption(optionName, defaultValue);
            case "spin" : {
                var mmMatch = spinPattern.matcher(payload);
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