package de.comroid.crystalshard.abstraction.serialization;

import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import de.comroid.crystalshard.abstraction.AbstractCloneable;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.CrystalShard.PLEASE_REPORT;

public class JsonBindings {
    private static abstract class Abstract<EXTR_DEF extends Comparable<EXTR_DEF> & Constable & ConstantDesc, JSON_TYPE extends JSON, STAGE_ONE, STAGE_TWO, TYPE_OUT>
            extends AbstractCloneable<JsonBinding<JSON_TYPE, STAGE_ONE, STAGE_TWO, TYPE_OUT>>
            implements JsonBinding<JSON_TYPE, STAGE_ONE, STAGE_TWO, TYPE_OUT> {
        protected final String fieldName;
        protected final BiFunction<JSON_TYPE, EXTR_DEF, STAGE_ONE> extractor;

        protected @Nullable Discord api;

        private Abstract(String fieldName, BiFunction<JSON_TYPE, EXTR_DEF, STAGE_ONE> extractor) {
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

        /**
         * Default method stub. This stub fails for every object that is not contraint to {@code Abstract&lb;String, ?, ?, ?, ?&rb;}
         *
         * @return {@link #fieldName} by default.
         */
        @Override
        @SuppressWarnings("unchecked")
        public STAGE_ONE extractValue(JSON_TYPE from) {
            return extractor.apply(from, (EXTR_DEF) fieldName);
        }
        
        protected STAGE_TWO mapStages(JSON_TYPE from) {
            throw new UnsupportedOperationException("Unused method stub."+PLEASE_REPORT);
        }
    }

    public static class OneStageImpl$Identity<T>
            extends Abstract<String, JSONObject, T, T, T>
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
        public T apply(JSONObject type_in) {
            return extractValue(type_in);
        }
    }

    public static class TwoStageImpl$Simple<S, T>
            extends Abstract<String, JSONObject, S, S, T>
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
        public T apply(JSONObject type_in) {
            return mapper.apply(extractValue(type_in));
        }
    }

    public static class TwoStageImpl$Api<S, T>
            extends Abstract<String, JSONObject, S, S, T>
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
        public T apply(JSONObject type_in) {
            return apiMapper.apply(api, extractValue(type_in));
        }
    }

    public static class TriStageImpl$UnderlyingObjects<T>
            extends Abstract<Integer, JSONArray, JSONObject, Stream<JSONObject>, Collection<T>>
            implements JsonBinding.TriStage<JSONObject, Collection<T>> {

        public TriStageImpl$UnderlyingObjects(String fieldName, Class<T> targetClass) {
            super(fieldName, JSONArray::getJSONObject);
            
            this.targetClass = targetClass;
        }

        @Override 
        @Contract(pure = true)
        public JsonBinding<JSONArray, JSONObject, Stream<JSONObject>, Collection<T>> clone() {
            return new TriStageImpl$UnderlyingObjects<>(fieldName, targetClass);
        }

        private final Class<T> targetClass;
        
        @Override
        public Collection<T> apply(JSONArray objects) {
            return mapStages(objects)
                    .map(json -> Adapter.create(targetClass, api, json))
                    .collect(Collectors.toList());
        }

        @Override
        protected Stream<JSONObject> mapStages(JSONArray from) {
            return from.stream().map(JSONObject.class::cast);
        }
    }
}