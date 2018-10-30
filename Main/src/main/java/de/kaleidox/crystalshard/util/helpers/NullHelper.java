package de.kaleidox.crystalshard.util.helpers;

import java.util.Objects;

public class NullHelper {
    // Static members
    // Static membe
    public static <T> T orDefault(T item, T def) {
        requireNonNull(item, def);
        return (item == null ? def : item);
    }

    public static void requireNonNull(Object... items) {
        for (Object item : items) {
            Objects.requireNonNull(item);
        }
    }

    protected static <T> Object requireNonNullElse(T test, T orElse) {
        if (Objects.isNull(test)) return orElse;
        return test;
    }
}
