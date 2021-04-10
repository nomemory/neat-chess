package net.andreinc.neatchess.client.model.option;

import java.util.Set;

public class ComboEngineOption extends EngineOption<String> {

    private Set<String> possibleOptions;

    public ComboEngineOption(String name, String defaultValue) {
        super(name, defaultValue);
    }

    public Set<String> getPossibleOptions() {
        return possibleOptions;
    }

    public void setPossibleOptions(Set<String> possibleOptions) {
        this.possibleOptions = possibleOptions;
    }

    @Override
    public String toString() {
        return "ComboEngineOption{" +
                "possibleOptions=" + possibleOptions +
                ", name='" + name + '\'' +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
