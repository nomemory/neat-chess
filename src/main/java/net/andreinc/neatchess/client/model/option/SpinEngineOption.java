package net.andreinc.neatchess.client.model.option;

public class SpinEngineOption extends EngineOption<Integer> {

    private Integer min;
    private Integer max;

    public SpinEngineOption(String name, Integer defaultValue) {
        super(name, defaultValue);
    }

    public SpinEngineOption(String name, Integer defaultValue, Integer min, Integer max) {
        this(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "SpinEngineOption{" +
                "name='" + name + '\'' +
                ", defaultValue=" + defaultValue +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
