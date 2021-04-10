package net.andreinc.neatchess.client.processor;

import net.andreinc.neatchess.client.model.EngineInfo;
import net.andreinc.neatchess.client.model.option.EngineOption;
import net.andreinc.neatchess.client.parser.EngineNameParser;
import net.andreinc.neatchess.client.parser.EngineOptionParser;

import java.util.List;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class EngineInfoProcessor extends UCICommandProcessor<EngineInfo> {

    protected static EngineNameParser engineNameParser = new EngineNameParser();
    protected static EngineOptionParser engineOptionParser = new EngineOptionParser();

    @Override
    public EngineInfo process(List<String> list) {
        final var engineName =
                list.stream()
                        .filter(engineNameParser::matches)
                        .map(engineNameParser::parse)
                        .findFirst()
                        .orElse("<<Unknown>>");
        final var options =
                list.stream()
                        .filter(engineOptionParser::matches)
                        .map(engineOptionParser::parse)
                        .collect(toMap(EngineOption::getName, identity()));
        return new EngineInfo(engineName, options);
    }
}
