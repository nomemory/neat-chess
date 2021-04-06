package net.andreinc.neatchess.client.processor;

import net.andreinc.neatchess.model.EngineInfo;
import net.andreinc.neatchess.model.option.EngineOption;

import java.util.List;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class EngineInfoProcessor extends AbstractProcessor<EngineInfo> {
    @Override
    public EngineInfo process(List<String> list) {
        final var engineName =
                list.stream()
                        .filter(s->s.startsWith("id name"))
                        .map(engineNameParser::parse)
                        .findFirst()
                        .orElse("<<Unknown>>");
        final var options =
                list.stream()
                        .filter(s -> s.startsWith("option name"))
                        .map(engineOptionParser::parse)
                        .collect(toMap(EngineOption::getName, identity()));
        return new EngineInfo(engineName, options);
    }
}
