package de.kaleidox.crystalshard.api.util;

import java.util.Optional;

public interface Castable<C> {
    default <T extends C> boolean canCastTo(Class<T> check) {
        return check.isAssignableFrom(this.getClass());
    }

    default <T extends C> T castOrNull(Class<T> castTo) {
        return castTo(castTo).orElse(null);
    }

    default <T extends C> Optional<T> castTo(Class<T> castTo) {
        return cast(castTo, this);
    }

    // Static members
    @SuppressWarnings("unchecked")
    static <T> Optional<T> cast(Class<T> castTo, Object instance) {
        return castTo.isAssignableFrom(instance.getClass()) ? Optional.of(castTo.cast(instance)) : Optional.empty();
    }
}