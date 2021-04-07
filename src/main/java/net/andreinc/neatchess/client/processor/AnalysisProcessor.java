package net.andreinc.neatchess.client.processor;

import net.andreinc.neatchess.model.Analysis;
import net.andreinc.neatchess.model.Move;
import net.andreinc.neatchess.parser.InfoDepthParser;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class AnalysisProcessor extends UCICommandProcessor<Analysis> {

    protected static InfoDepthParser infoDepthParser = new InfoDepthParser();
    protected static BestMoveProcessor bestMoveProcessor = new BestMoveProcessor();

    @Override
    public Analysis process(List<String> list) {
        var bestMove = bestMoveProcessor.process(list);
//        Map<Integer, Move> map = new TreeMap<Integer, Move>();
//        for (String line : list) {
//            System.out.println(line);
//            if (infoDepthParser.matches(line)) {
//                System.out.println("\t" + infoDepthParser.matches(line));
//                var m = infoDepthParser.parse(line);
//                map.put(m.getPv(), m);
//            }
//        }
//        return new Analysis(bestMove, map);
        list.forEach(System.out::println);
        System.out.println(list.size());
        System.out.println("----");
        list.stream().filter(infoDepthParser::matches).map(infoDepthParser::parse).collect(Collectors.groupingBy(Move::getPv)).entrySet().forEach(System.out::println);
        list.stream().filter(infoDepthParser::matches).map(infoDepthParser::parse).collect(Collectors.toMap(Move::getPv, Move::getContinuation)).entrySet().forEach(System.out::println);
//                var map =
//                        list.stream()
//                            .map(s ->
//                            {
//                                System.out.println(s + " " + infoDepthParser.matches(s));
//                                return s;
//                            })
//                            .filter(s -> {
////                                System.out.println(s + " " + infoDepthParser.matches(s));
//                                return infoDepthParser.matches(s);
//                            })
//                            .map(infoDepthParser::parse)
//                            .collect(toMap(Move::getPv, Function.identity()));
        System.out.println("ads");
                var result = new Analysis(bestMove, null);
                return result;
    }
}
