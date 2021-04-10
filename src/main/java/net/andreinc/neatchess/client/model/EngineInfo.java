package net.andreinc.neatchess.client.model;

import net.andreinc.neatchess.client.model.option.EngineOption;

import java.util.Map;

public class EngineInfo {
    private final String name;
    private final Map<String, EngineOption> options;

    public EngineInfo(String name, Map<String, EngineOption> options) {
        this.name = name;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public Map<String, EngineOption> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "EngineInfo{" +
                "name='" + name + '\'' +
                ", options=" + options +
                '}';
    }
}
