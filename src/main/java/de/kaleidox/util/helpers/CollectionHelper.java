package de.kaleidox.util.helpers;

import java.util.Collection;
import java.util.Objects;

public class CollectionHelper extends NullHelper {
    // Static members
    // Static membe
    public static <T> Collection<T> requireNoNull(Collection<T> collection) {
        for (Object item : collection) {
            Objects.requireNonNull(item);
        }
        return collection;
    }
    
    static void nullChecks(Collection<?>... lists) {
        for (Collection<?> x : lists) {
            Objects.requireNonNull(x);
            requireNoNull(x);
        }
    }
}
