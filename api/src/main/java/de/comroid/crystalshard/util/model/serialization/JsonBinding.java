package de.comroid.crystalshard.util.model.serialization;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

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

public interface JsonBinding<JSON_TYPE extends JSON, STAGE_ONE, STAGE_TWO, TYPE_OUT> extends Function<STAGE_ONE, TYPE_OUT> {
    @NotNull String fieldName();

    @Contract("null -> fail; _ -> _")
    JsonBinding<JSON_TYPE, STAGE_ONE, STAGE_TWO, TYPE_OUT> cloneWithApi(Discord api);

    STAGE_ONE extractValue(JSON_TYPE from);

    static <T> OneStage<T> identity(String fieldName, BiFunction<JSONObject, String, T> extractor) {
        return Adapter.require(JsonBinding.OneStage.class, fieldName, extractor);
    }

    static <S, T> TwoStage<S, T> simple(String fieldName, BiFunction<JSONObject, String, S> extractor, Function<S, T> mapper) {
        return Adapter.require(JsonBinding.TwoStage.class, fieldName, extractor, mapper);
    }

    static <S, T> TwoStage<S, T> api(String fieldName, BiFunction<JSONObject, String, S> extractor, BiFunction<Discord, S, T> apiMapper) {
        return Adapter.require(JsonBinding.TwoStage.class, fieldName, extractor, apiMapper);
    }

    static <T extends Cacheable & Snowflake> TwoStage<Long, T> cache(String fieldName, BiFunction<CacheManager, Long, Optional<T>> cacheMapper) {
        return api(fieldName, JSONObject::getLong, (api, id) -> cacheMapper.apply(api.getCacheManager(), id)
                .orElseThrow(() -> new AssertionError("No instance of " + fieldName + " was found in cache!")));
    }

    static <S, T> TriStage<S, Collection<T>> underlyingMappingCollection(String fieldName, BiFunction<JSONObject, String, S> extractor, BiFunction<Discord, S, T> eachMapper) {
        return Adapter.require(JsonBinding.class, fieldName, extractor, eachMapper);
    }
    
    interface OneStage<T> extends JsonBinding<JSONObject, T, T, T> {
    }

    interface TwoStage<S, T> extends JsonBinding<JSONObject, S, S, T> {
    }

    interface TriStage<S, T> extends JsonBinding<JSONArray, List<S>, List<S>, Collection<T>> {
    }
}
