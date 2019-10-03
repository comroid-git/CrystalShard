package de.kaleidox.crystalshard.abstraction.serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
implementations required:

SimpleJsonTrait(Function<JsonNode, Object> extractor, String fieldName, Function<S, T> mapper)
ComplexJsonTrait(Function<JsonNode, Object> extractor, String fieldName, BiFunction<Discord, S, T> apiMapper) 
CollectiveJsonTrait(String fieldName, Class<T extends JsonDeserializable> targetClass)
 */
public abstract class JsonTraitImpl<S, T> implements JsonTrait<S, T> {
    protected final String fieldName;
    private final Function<JsonNode, Object> extractor;
    protected @Nullable Discord api;

    public JsonTraitImpl(Function<JsonNode, Object> extractor, String fieldName) {
        this.extractor = extractor;
        this.fieldName = fieldName;
    }

    @Override
    public void withApi(Discord api) {
        this.api = api;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    @Override
    public Object extract(JsonNode from) {
        if (from.isNull())
            return null;

        return extractor.apply(from);
    }

    public static class SimpleJsonTrait<S, T> extends JsonTraitImpl<S, T> {
        private final Function<S, T> mapper;

        public SimpleJsonTrait(Function<JsonNode, Object> extractor, String fieldName, Function<S, T> mapper) {
            super(extractor, fieldName);

            this.mapper = mapper;
        }

        @Override
        public @Nullable T map(S value) {
            return mapper.apply(value);
        }
    }

    public static class ComplexJsonTrait<S, T> extends JsonTraitImpl<S, T> {
        private final BiFunction<Discord, S, T> apiMapper;

        public ComplexJsonTrait(Function<JsonNode, Object> extractor, String fieldName, BiFunction<Discord, S, T> apiMapper) {
            super(extractor, fieldName);
            this.apiMapper = apiMapper;
        }

        @Override
        public @Nullable T map(S value) {
            return apiMapper.apply(api, value);
        }
    }

    public static class CollectiveJsonTrait<T extends JsonDeserializable> extends JsonTraitImpl<ArrayNode, Collection<T>> {
        private final Class<T> targetClass;

        public CollectiveJsonTrait(String fieldName, Class<T> targetClass) {
            super(null, fieldName);
            this.targetClass = targetClass;
        }

        @Override
        public Object extract(JsonNode from) {
            if (from.isNull())
                return null;

            JsonNode target = from.path(fieldName);

            if (target.isArray())
                //noinspection RedundantCast
                return (ArrayNode) target;

            throw new NoSuchElementException("No JSON ArrayNode was found at path \"" + fieldName + "\"");
        }

        @Override
        public @NotNull Collection<T> map(ArrayNode value) {
            if (value == null) return Collections.emptyList();

            Collection<T> yields = new ArrayList<>();

            for (JsonNode data : value) {
                T yield = Adapter.create(targetClass, api, data);

                yields.add(yield);
            }

            return Collections.unmodifiableCollection(yields);
        }
    }
}
