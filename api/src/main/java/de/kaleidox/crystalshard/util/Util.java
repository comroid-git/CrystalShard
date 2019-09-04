package de.kaleidox.crystalshard.util;

public final class Util {
    public static <T, R> R hackCast(T var) {
        //noinspection unchecked,RedundantCast
        return (R) (Object) var;
    }
}
