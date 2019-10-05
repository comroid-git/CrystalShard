package de.kaleidox.crystalshard.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Util {
    public static <T, R> R hackCast(T var) {
        //noinspection unchecked,RedundantCast
        return (R) (Object) var;
    }

    @SafeVarargs
    public static <T> Stream<T> quickStream(int threshold, T... objects) {
        return objects.length > threshold
                ? StreamSupport.stream(Arrays.spliterator(objects), true)
                : Stream.of(objects);
    }

    public static <T> Stream<T> quickStream(int threshold, Collection<T> collection) {
        return collection.size() > threshold
                ? collection.parallelStream()
                : collection.stream();
    }

    public static <T> int arrayLocate(T[] array, T target, BiFunction<T, T, Boolean> comparator) {
        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            
            if (comparator.apply(target, t))
                return i;   
        }

        return -1;
    }
}
