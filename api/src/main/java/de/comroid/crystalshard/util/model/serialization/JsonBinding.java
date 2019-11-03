package de.comroid.crystalshard.util.model.serialization;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.core.api.cache.Cacheable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface JsonBinding<JSON_TYPE extends JSON, STAGE_ONE, STAGE_TWO, TYPE_OUT> extends Function<JSON_TYPE, TYPE_OUT> {
    @NotNull String fieldName();

    @Contract("null -> fail; _ -> _")
    JsonBinding<JSON_TYPE, STAGE_ONE, STAGE_TWO, TYPE_OUT> cloneWithApi(Discord api);

    STAGE_ONE extractValue(JSON_TYPE from);

    static <T> OneStage<T> identity(String fieldName, BiFunction<JSONObject, String, S> extractor) {
        return Adapter.create(JsonBinding.OneStage.class, fieldName, extractor);
    }

    static <S, T> TwoStage<S, T> simple(String fieldName, BiFunction<JSONObject, String, S> extractor, Function<S, T> mapper) {
        return Adapter.create(JsonBinding.TwoStage.class, fieldName, extractor, mapper);
    }

    static <S, T> TwoStage<S, T> api(String fieldName, BiFunction<JSONObject, String, S> extractor, BiFunction<Discord, S, T> apiMapper) {
        return Adapter.create(JsonBinding.TwoStage.class, fieldName, extractor, apiMapper);
    }

    static <T extends Cacheable & Snowflake> TwoStage<Long, T> cache(String fieldName, BiFunction<CacheManager, Long, Optional<T>> cacheMapper) {
        return api(fieldName, JSONObject::getLong, (api, id) -> cacheMapper.apply(api.getCacheManager(), id)
                .orElseThrow(() -> new AssertionError("No instance of " + fieldName + " was found in cache!")));
    }

    static <S, T extends JsonDeserializable> TriStage<Collection<T>> underlyingCacheable(String fieldName, Class<T> targetClass) {
        return Adapter.create(JsonBinding.TriStage.class, fieldName, targetClass);
    }

    static <T extends JsonDeserializable>  underlyingObject(String fieldName, Class<T> targetClass
    ) {
        return Adapter.create(JsonBinding.class, fieldName, targetClass);
    }

    static <T extends JsonDeserializable> JsonBinding<ArrayNode, Collection<T>> underlyingCollective(
            String fieldName,
            Class<T> targetClass
    ) {
        //noinspection unchecked
        return underlyingCollective(fieldName, targetClass, (api, id) -> Adapter.access(targetClass, api, api, id));
    }

    static <T> JsonBinding<ArrayNode, Collection<T>> underlyingCollective(
            String fieldName,
            Class<T> targetClass,
            BiFunction<Discord, JsonNode, T> eachMapper
    ) {
        return Adapter.create(JsonBinding.class, fieldName, eachMapper, targetClass);
    }
    
    interface OneStage<T> extends JsonBinding<JSONObject, T, T, T> {
    }

    interface TwoStage<S, T> extends JsonBinding<JSONObject, S, S, T> {
    }
    interface TriStage<S, T> extends JsonBinding<JSONArray, S, Stream<S>, T> {
    }
}
