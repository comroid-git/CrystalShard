package de.comroid.crystalshard.util.model.serialization;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.core.api.cache.Cacheable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jetbrains.annotations.Nullable;

public interface JsonTrait<J, T> extends Function<J, T> {
    JsonTrait<J, T> withApi(Discord api);

    String fieldName();

    Object extract(JsonNode from);

    @Override
    @Nullable T apply(J value);

    default Optional<T> wrap(J value) {
        return Optional.ofNullable(apply(value));
    }

    static <X> JsonTrait<X, X> identity(Function<JsonNode, X> extractor, String fieldName) {
        return simple(extractor, fieldName, Function.identity());
    }

    static <J, T> JsonTrait<J, T> simple(Function<JsonNode, J> extractor, String fieldName, Function<J, T> mapper) {
        return Adapter.create(JsonTrait.class, extractor, fieldName, mapper);
    }

    static <J, T> JsonTrait<J, T> api(Function<JsonNode, J> extractor, String fieldName, BiFunction<Discord, J, T> apiMapper) {
        return Adapter.create(JsonTrait.class, extractor, fieldName, apiMapper);
    }

    static <T extends Cacheable & Snowflake> JsonTrait<Long, T> cache(
            String fieldName,
            BiFunction<CacheManager, Long, Optional<T>> cacheMapper
    ) {
        return api(JsonNode::asLong, fieldName, (api, id) -> cacheMapper.apply(api.getCacheManager(), id)
                .orElseThrow(() -> new AssertionError("No instance of " + fieldName + " was found in cache!")));
    }

    static <T extends JsonDeserializable> JsonTrait<ArrayNode, Collection<T>> collective(
            String fieldName,
            Class<T> targetClass
    ) {
        return Adapter.create(JsonTrait.class, fieldName, targetClass, 0);
    }

    static <T extends JsonDeserializable> JsonTrait<JsonNode, T> underlying(
            String fieldName,
            Class<T> targetClass
    ) {
        return Adapter.create(JsonTrait.class, fieldName, targetClass);
    }

    static <T extends JsonDeserializable> JsonTrait<ArrayNode, Collection<T>> underlyingCollective(
            String fieldName,
            Class<T> targetClass
    ) {
        //noinspection unchecked
        return underlyingCollective(fieldName, targetClass, (api, id) -> Adapter.access(targetClass, api, api, id));
    }

    static <T> JsonTrait<ArrayNode, Collection<T>> underlyingCollective(
            String fieldName,
            Class<T> targetClass,
            BiFunction<Discord, JsonNode, T> eachMapper
    ) {
        return Adapter.create(JsonTrait.class, fieldName, eachMapper, targetClass);
    }
}
