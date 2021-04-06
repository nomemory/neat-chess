package net.andreinc.neatchess.client.processor;

import net.andreinc.neatchess.exception.UCIRuntimeException;
import net.andreinc.neatchess.model.BestMove;

import java.util.List;

public class BestMoveProcessor extends AbstractProcessor<BestMove> {
    @Override
    public BestMove process(List<String> list) {
        return list.stream()
                .filter(s->s.startsWith("bestmove"))
                .findFirst()
                .map(bestMoveParser::parse)
                .orElseThrow(()->new UCIRuntimeException("Cannot find best movement in engine output!\n"));
    }
}
