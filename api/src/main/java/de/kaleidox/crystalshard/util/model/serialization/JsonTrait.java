package de.kaleidox.crystalshard.util.model.serialization;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jetbrains.annotations.Nullable;

public interface JsonTrait<S, T> {
    void withApi(Discord api);

    String fieldName();

    Object extract(JsonNode from);

    @Nullable T map(S value);

    default Optional<T> wrap(S value) {
        return Optional.ofNullable(map(value));
    }

    static <X> JsonTrait<X, X> identity(Function<JsonNode, Object> extractor, String fieldName) {
        return simple(extractor, fieldName, Function.identity());
    }

    static <S, T> JsonTrait<S, T> simple(Function<JsonNode, Object> extractor, String fieldName, Function<S, T> mapper) {
        return Adapter.create(JsonTrait.class, extractor, fieldName, mapper);
    }

    static <S, T> JsonTrait<S, T> api(Function<JsonNode, Object> extractor, String fieldName, BiFunction<Discord, S, T> apiMapper) {
        return Adapter.create(JsonTrait.class, extractor, fieldName, apiMapper);
    }

    static <T extends Cacheable & Snowflake> JsonTrait<Long, T> cache(
            Function<JsonNode, Object> extractor,
            String fieldName,
            BiFunction<CacheManager, Long, Optional<T>> cacheMapper
    ) {
        return api(extractor, fieldName, (api, id) -> cacheMapper.apply(api.getCacheManager(), id)
                .orElseThrow(() -> new AssertionError("No instance of " + fieldName + " was found in cache!")));
    }

    static <T extends JsonDeserializable> JsonTrait<ArrayNode, Collection<T>> collective(
            String fieldName,
            Class<T> targetClass
    ) {
        return Adapter.create(JsonTrait.class, fieldName, targetClass);
    }
}
