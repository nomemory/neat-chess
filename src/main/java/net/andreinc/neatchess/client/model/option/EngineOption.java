package net.andreinc.neatchess.client.model.option;

public abstract class EngineOption<T> {

    protected String name;
    protected T defaultValue;

    public EngineOption(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
