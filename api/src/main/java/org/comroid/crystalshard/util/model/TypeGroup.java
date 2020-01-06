package org.comroid.crystalshard.util.model;

import java.util.Optional;

public interface TypeGroup<T> {
    default <R extends T> Optional<R> as(Class<R> type) {
        return cast(this, type);
    }

    default boolean isType(Class<?> type) {
        return canCast(this, type);
    }

    static <T, R extends T> Optional<R> cast(T inst, Class<R> type) {
        if (canCast(inst, type))
            return Optional.of(type.cast(inst));

        return Optional.empty();
    }

    static <T> boolean canCast(T inst, Class<?> type) {
        return type.isInstance(inst);
    }
}
