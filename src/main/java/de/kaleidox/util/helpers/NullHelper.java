package de.kaleidox.util.helpers;

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
}
