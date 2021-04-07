package net.andreinc.neatchess.client.processor;

import net.andreinc.neatchess.parser.BestMoveParser;
import net.andreinc.neatchess.parser.EngineNameParser;
import net.andreinc.neatchess.parser.EngineOptionParser;
import net.andreinc.neatchess.parser.InfoDepthParser;

import java.util.List;

public abstract class UCICommandProcessor<T> {
    public abstract T process(List<String> list);
}
