package de.kaleidox.crystalshard.core.api.cache;

import java.util.Optional;
import java.util.OptionalLong;

import com.fasterxml.jackson.databind.JsonNode;

public interface Cacheable {
    void update(JsonNode data);

    default Optional<Long> getCacheParentID() {
        return OptionalLong.empty();
    }

    default Optional<Class<? extends Cacheable>> getCacheParentType() {
        return Optional.empty();
    }

    default boolean isSingletonType() {
        return false;
    }

    default boolean isSubcacheMember() {
        return getCacheParentType().isPresent();
    }
}
