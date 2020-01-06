package org.comroid.crystalshard.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Util {
    public static URL createUrl$rethrow(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new AssertionError("Unexpected MalformedURLException", e);
        }
    }

    public static <T, R> R hackCast(T var) { // todo rename
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

    public static boolean isFlagSet(int flag, int inMask) {
        return (inMask & flag) != 0;
    }
}
