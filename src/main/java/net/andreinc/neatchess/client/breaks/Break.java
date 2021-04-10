package net.andreinc.neatchess.client.breaks;

import java.util.function.Predicate;

public abstract class Break {
    /**
     * This method represents the "breaking" for the while(true) condition that captures the UCI output.
     *
     * @param value The string that it's contained in on the breaking row (e.g.: "uciok")
     *
     * @return True or false depending if the loops has to break on input or not
     */
    public static Predicate<String> breakOn(String value) {
        return s -> s.startsWith(value);
    }
}
