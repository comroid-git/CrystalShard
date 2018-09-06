package de.kaleidox.util.helpers;

import java.util.Collection;
import java.util.Objects;

public class CollectionHelper {
    public static <T> Collection<T> requireNoNull(Collection<T> collection) {
        for (Object item : collection) {
            Objects.requireNonNull(item);
        }
        return collection;
    }
}
