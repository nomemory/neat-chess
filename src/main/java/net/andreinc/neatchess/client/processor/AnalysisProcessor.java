package net.andreinc.neatchess.client.processor;

import net.andreinc.neatchess.client.model.Analysis;
import net.andreinc.neatchess.client.model.Move;
import net.andreinc.neatchess.client.parser.InfoDepthParser;

import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class AnalysisProcessor extends UCICommandProcessor<Analysis> {
    protected static InfoDepthParser infoDepthParser = new InfoDepthParser();
    @Override
    public Analysis process(List<String> list) {
        var map =
                list.stream()
                    .filter(infoDepthParser::matches)
                    .map(infoDepthParser::parse)
                    .collect(
                        toMap(
                            Move::getPv,
                            Function.identity(),
                            (existing, replacement) -> replacement,
                            TreeMap::new
                        )
                    );
        if (map.isEmpty() && list.stream().anyMatch(s -> s.startsWith("info depth 0 score mate 0"))) {
            return new Analysis(map, true);
        }
        return new Analysis(map);
    }
}
