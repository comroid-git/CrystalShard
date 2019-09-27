package de.kaleidox.crystalshard.util.model.serialization;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.model.ApiBound;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

public interface JsonTrait<S, T> extends ApiBound {
    @Nullable S getSourceValue();

    Class<S> getSourceClass();

    @Nullable T getTargetValue();

    Class<T> getTargetClass();

    static <X> JsonTrait<X, X> identity(String fieldName) {
        return simple(fieldName, Function.identity());
    }

    static <S, T> JsonTrait<S, T> simple(String fieldName, Function<S, T> mapper) {
        return Adapter.create(JsonTrait.class, fieldName, mapper);
    }

    static <S, T> JsonTrait<S, T> complex(String fieldName, BiFunction<Discord, S, T> apiMapper) {
        return Adapter.create(JsonTrait.class, fieldName, apiMapper);
    }

    static <T extends Cacheable & Snowflake> JsonTrait<Long, T> cache(
            String fieldName,
            BiFunction<CacheManager, Long, Optional<T>> cacheMapper
    ) {
        return complex(fieldName, (api, id) -> cacheMapper.apply(api.getCacheManager(), id)
                .orElseThrow(() -> new AssertionError("No instance of " + fieldName + " was found in cache!")));
    }

    static <T extends JsonDeserializable> JsonTrait<JsonNode, Collection<T>> collective(String fieldName, Class<T> targetClass) {
        return Adapter.create(JsonTrait.class, fieldName, targetClass);
    }
}
