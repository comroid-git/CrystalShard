package de.comroid.crystalshard.util;

import java.util.Objects;
import java.util.stream.Stream;

public class ThreadpoolArrayUtil {
    public static <T> boolean add(T it, T[] to) {
        int i = 0;
        while (i < to.length && to[i] != null) i++;
        if (i == to.length) return false;
        to[i] = it;
        return true;
    }

    public static <T> int remove(T it, T[] from) {
        final int i = find(it, from);
        from[i] = null;
        return i;
    }
    
    public static <T> int find(T it, T[] in) {
        int i = 0;
        while (i < in.length && !in[i].equals(it)) i++;
        if (i == in.length)
            return -1;
        else return i;
    }

    public static <T> boolean allNull(T[] array) {
        for (T each : array)
            if (each != null)
                return false;
        
        return true;
    }

    public static <T> long notNullSum(Object[]... arrays) {
        return Stream.of(arrays)
                .mapToLong(array -> Stream.of(array)
                        .filter(Objects::nonNull)
                        .count())
                .sum();
    }
}
