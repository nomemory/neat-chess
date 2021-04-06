package net.andreinc.neatchess.client.processor;

import net.andreinc.neatchess.parser.BestMoveParser;
import net.andreinc.neatchess.parser.EngineNameParser;
import net.andreinc.neatchess.parser.EngineOptionParser;
import net.andreinc.neatchess.parser.InfoDepthParser;

import java.util.List;

public abstract class AbstractProcessor<T> {

    protected static BestMoveParser bestMoveParser = new BestMoveParser();
    protected static EngineNameParser engineNameParser = new EngineNameParser();
    protected static EngineOptionParser engineOptionParser = new EngineOptionParser();
    protected static InfoDepthParser infoDepthParser = new InfoDepthParser();

    public abstract T process(List<String> list);
}
