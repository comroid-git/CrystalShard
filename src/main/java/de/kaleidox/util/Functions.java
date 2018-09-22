package de.kaleidox.util;

import java.util.function.Function;

public final class Functions {
// Static members
    // Static membe
    public static <T> Function<T, T> sameItem() {
        return item -> item;
    }
    
    public static <T> T sameItem(T item) {
        return item;
    }
    
    public static Function<String, Integer> stringToInteger() {
        return Integer::parseInt;
    }
    
    public static int stringToInteger(String string) {
        return Integer.parseInt(string);
    }
    
    public static Function<String, Long> stringToLong() {
        return Long::parseLong;
    }
    
    public static long stringToLong(String string) {
        return Long.parseLong(string);
    }
}
