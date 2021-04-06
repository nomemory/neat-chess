package net.andreinc.neatchess.client.processor;

import net.andreinc.neatchess.model.Analysis;
import net.andreinc.neatchess.model.BestMove;

import java.util.List;

public class AnalysisProcessor extends AbstractProcessor<Analysis> {
    @Override
    public Analysis process(List<String> list) {
        var analysis = new Analysis();
        BestMove bestMove = null;
        for (String line : list) {
        }
        return analysis;
    }
}
