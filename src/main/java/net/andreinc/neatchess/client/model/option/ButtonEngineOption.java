package net.andreinc.neatchess.client.model.option;

public class ButtonEngineOption extends EngineOption<String> {

    public ButtonEngineOption(String name, String defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public String toString() {
        return "ButtonEngineOption{" +
                "name='" + name + '\'' +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
