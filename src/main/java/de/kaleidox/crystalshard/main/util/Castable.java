package de.kaleidox.crystalshard.main.util;

import java.util.Optional;

public interface Castable<C> {
    default <T extends C> Optional<T> castTo(Class<T> castTo) {
        return cast(castTo, this);
    }

    default <T extends C> boolean canCastTo(Class<T> check) {
        return check.isAssignableFrom(this.getClass());
    }

    @SuppressWarnings("unchecked")
    private static <T> Optional<T> cast(Class<T> castTo, Object instance) {
        return castTo.isAssignableFrom(instance.getClass()) ?
                Optional.of((T) instance) : Optional.empty();
    }
}