package de.comroid.crystalshard.abstraction.serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.comroid.crystalshard.abstraction.AbstractCloneable;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.CrystalShard.PLEASE_REPORT;

public class JsonBindings {
    private static abstract class Abstract<JSON_TYPE extends JSON, STAGE_ONE, STAGE_TWO, TYPE_OUT>
            extends AbstractCloneable<JsonBinding<JSON_TYPE, STAGE_ONE, STAGE_TWO, TYPE_OUT>>
            implements JsonBinding<JSON_TYPE, STAGE_ONE, STAGE_TWO, TYPE_OUT> {
        protected final String fieldName;
        protected final BiFunction<JSON_TYPE, String, STAGE_ONE> extractor;

        protected @Nullable Discord api;

        private Abstract(String fieldName, BiFunction<JSON_TYPE, String, STAGE_ONE> extractor) {
            this.fieldName = fieldName;
            this.extractor = extractor;
        }

        @Override
        public @NotNull String fieldName() {
            return fieldName;
        }

        @Override
        @Contract("null -> fail; _ -> _")
        public JsonBinding<JSON_TYPE, STAGE_ONE, STAGE_TWO, TYPE_OUT> cloneWithApi(Discord api) {
            if (api != null)
                throw new UnsupportedOperationException("Cannot clone this instance!" + PLEASE_REPORT);

            final JsonBinding<JSON_TYPE, STAGE_ONE, STAGE_TWO, TYPE_OUT> clone = clone();

            if (clone instanceof Abstract)
                ((Abstract) clone).api = api;
            else throw new UnsupportedOperationException("Clone not supported properly." + PLEASE_REPORT);

            return clone;
        }

        @Override
        public STAGE_ONE extractValue(JSON_TYPE from) {
            return extractor.apply(from, fieldName);
        }
        
        protected STAGE_TWO mapStages(JSON_TYPE from) {
            throw new UnsupportedOperationException("Unused method stub."+PLEASE_REPORT);
        }
    }

    public static class OneStageImpl$Identity<T>
            extends Abstract<JSONObject, T, T, T>
            implements JsonBinding.OneStage<T> {

        public OneStageImpl$Identity(String fieldName, BiFunction<JSONObject, String, T> extractor) {
            super(fieldName, extractor);
        }

        @Override
        @Contract(pure = true)
        public JsonBinding<JSONObject, T, T, T> clone() {
            return new OneStageImpl$Identity<>(fieldName, extractor);
        }

        @Override
        @Contract("null -> null; _ -> _")
        public T apply(T type_in) {
            return type_in;
        }
    }

    public static class TwoStageImpl$Simple<S, T>
            extends Abstract<JSONObject, S, S, T>
            implements JsonBinding.TwoStage<S, T> {
        private final Function<S, T> mapper;

        public TwoStageImpl$Simple(String fieldName, BiFunction<JSONObject, String, S> extractor, Function<S, T> mapper) {
            super(fieldName, extractor);

            this.mapper = mapper;
        }

        @Override
        @Contract(pure = true)
        public JsonBinding<JSONObject, S, S, T> clone() {
            return new TwoStageImpl$Simple<>(fieldName, extractor, mapper);
        }

        @Override
        @Contract("null -> null; _ -> _")
        public T apply(S type_in) {
            return mapper.apply(type_in);
        }
    }

    public static class TwoStageImpl$Api<S, T>
            extends Abstract<JSONObject, S, S, T>
            implements JsonBinding.TwoStage<S, T> {
        private final BiFunction<Discord, S, T> apiMapper;

        private TwoStageImpl$Api(String fieldName, BiFunction<JSONObject, String, S> extractor, BiFunction<Discord, S, T> apiMapper) {
            super(fieldName, extractor);

            this.apiMapper = apiMapper;
        }

        @Override
        @Contract(pure = true)
        public JsonBinding<JSONObject, S, S, T> clone() {
            return new TwoStageImpl$Api<>(fieldName, extractor, apiMapper);
        }

        @Override
        @Contract("null -> null; _ -> _")
        public T apply(S type_in) {
            return apiMapper.apply(api, type_in);
        }
    }

    public static class TriStageImpl$UnderlyingMapped<S, T>
            extends Abstract<JSONArray, List<S>, List<S>, Collection<T>>
            implements JsonBinding.TriStage<S, T> {
        private final BiFunction<Discord, S, T> eachMapper;

        public TriStageImpl$UnderlyingMapped(String fieldName, BiFunction<JSONArray, String, List<S>> extractor, BiFunction<Discord, S, T> eachMapper) {
            super(fieldName, (json, unused) -> new ArrayList<>() {{
                //noinspection unchecked
                json.forEach(it -> add((S) it));
            }});

            this.eachMapper = eachMapper;
        }

        @Override
        @Contract(pure = true)
        public JsonBinding<JSONArray, List<S>, List<S>, Collection<T>> clone() {
            return new TriStageImpl$UnderlyingMapped<>(fieldName, extractor, eachMapper);
        }

        @Override
        public Collection<T> apply(List<S> type_in) {
            return Util.quickStream(250, type_in)
                    .map(it -> eachMapper.apply(api, it))
                    .collect(Collectors.toList());
        }
    }
}