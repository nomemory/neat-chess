package net.andreinc.neatchess.client.breaks;

import java.util.function.Predicate;

public abstract class Break {
    public static Predicate<String> breakOn(String value) {
        return s -> s.startsWith(value);
    }
}
