package de.kaleidox.crystalshard.abstraction.serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.kaleidox.crystalshard.CrystalShard;
import de.kaleidox.crystalshard.abstraction.AbstractCloneable;
import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;
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
UnderlyingObjectJsonTrait(String fieldName, Function<T, O> outputMapper, Class<T> targetClass)
 */
public abstract class JsonTraitImpl<J, T> extends AbstractCloneable<JsonTrait<J, T>> implements JsonTrait<J, T> {
    protected final Function<JsonNode, J> extractor;

    protected final String fieldName;
    protected @Nullable Discord api;
    protected @Nullable T val;

    public JsonTraitImpl(Function<JsonNode, J> extractor, String fieldName) {
        super();

        this.extractor = extractor;
        this.fieldName = fieldName;
    }

    @Override
    public JsonTrait<J, T> withApi(Discord api) {
        JsonTrait<J, T> clone = clone();
        ((JsonTraitImpl) clone).api = api;

        return clone;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    @Override
    public J extract(JsonNode from) {
        if (from.isNull())
            return null;

        return extractor.apply(from.path(fieldName));
    }

    @Override
    public T apply(final J from) {
        // if val EXISTS & IS deserializable & from IS node 
        if (val instanceof JsonDeserializable && from instanceof JsonNode) {
            // then update old val
            ((JsonDeserializable) val).updateFromJson((JsonNode) from);

            return val;
        }

        // otherwise simply remap and store
        return (val = map(from));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JsonTraitImpl)
            return super.equals(obj);

        return false;
    }

    protected abstract T map(J with);

    public static class SimpleJsonTrait<J, T> extends JsonTraitImpl<J, T> {
        private final Function<J, T> mapper;

        public SimpleJsonTrait(Function<JsonNode, J> extractor, String fieldName, Function<J, T> mapper) {
            super(extractor, fieldName);

            this.mapper = mapper;
        }

        @Override
        public @Nullable T map(J value) {
            return mapper.apply(value);
        }

        @Override
        public JsonTrait<J, T> clone() {
            return new SimpleJsonTrait<>(extractor, fieldName, mapper).equalize(this);
        }
    }

    public static class ComplexJsonTrait<J, T> extends JsonTraitImpl<J, T> {
        private final BiFunction<Discord, J, T> apiMapper;

        public ComplexJsonTrait(Function<JsonNode, J> extractor, String fieldName, BiFunction<Discord, J, T> apiMapper) {
            super(extractor, fieldName);
            this.apiMapper = apiMapper;
        }

        @Override
        public @Nullable T map(J value) {
            if (api == null)
                throw new IllegalStateException("API is null! Please open a bug report at " + CrystalShard.ISSUES_URL);

            return apiMapper.apply(api, value);
        }

        @Override
        public JsonTrait<J, T> clone() {
            return new ComplexJsonTrait<>(extractor, fieldName, apiMapper).equalize(this);
        }
    }

    public static class CollectiveJsonTrait<T extends JsonDeserializable> extends JsonTraitImpl<ArrayNode, Collection<T>> {
        private final Class<T> targetClass;

        // unused argument for adapter compatibility
        public CollectiveJsonTrait(String fieldName, Class<T> targetClass, int unused) {
            super(null, fieldName);
            this.targetClass = targetClass;
        }

        @Override
        public ArrayNode extract(JsonNode from) {
            if (from.isNull())
                return null;

            JsonNode target = from.path(fieldName);

            if (target.isArray())
                return (ArrayNode) target;

            throw new NoSuchElementException("No JSON ArrayNode was found at path \"" + fieldName + "\"");
        }

        @Override
        public @NotNull Collection<T> map(ArrayNode value) {
            if (api == null)
                throw new IllegalStateException("API is null! Please open a bug report at " + CrystalShard.ISSUES_URL);

            if (value == null) return Collections.emptyList();

            Collection<T> yields = new ArrayList<>();

            for (JsonNode data : value) {
                T yield = Adapter.create(targetClass, api, data);

                yields.add(yield);
            }

            return Collections.unmodifiableCollection(yields);
        }

        @Override
        public JsonTrait<ArrayNode, Collection<T>> clone() {
            return new CollectiveJsonTrait<>(fieldName, targetClass, 0).equalize(this);
        }
    }

    public static class UnderlyingCollectionJsonTrait<T extends Snowflake> extends JsonTraitImpl<ArrayNode, Collection<T>> {
        private final Class<T> targetClass;

        // unused argument for adapter compatibility
        public UnderlyingCollectionJsonTrait(String fieldName, Class<T> targetClass, char unused) {
            super(null, fieldName);
            this.targetClass = targetClass;
        }

        @Override
        public ArrayNode extract(JsonNode from) {
            if (from.isNull())
                return null;

            JsonNode target = from.path(fieldName);

            if (target.isArray())
                return (ArrayNode) target;

            throw new NoSuchElementException("No JSON ArrayNode was found at path \"" + fieldName + "\"");
        }

        @Override
        public @Nullable Collection<T> map(ArrayNode value) {
            if (api == null)
                throw new IllegalStateException("API is null! Please open a bug report at " + CrystalShard.ISSUES_URL);

            if (value == null) return Collections.emptyList();

            Collection<T> yields = new ArrayList<>();

            for (JsonNode data : value) {
                T yield = Adapter.access(targetClass, api, api, data);

                yields.add(yield);
            }

            return Collections.unmodifiableCollection(yields);
        }

        @Override
        public JsonTrait<ArrayNode, Collection<T>> clone() {
            return new UnderlyingCollectionJsonTrait<>(fieldName, targetClass, (char) 0).equalize(this);
        }
    }

    public static class UnderlyingObjectJsonTrait<T extends Snowflake> extends JsonTraitImpl<JsonNode, T> {
        private final BiFunction<Discord, JsonNode, T> eachMapper;
        private final Class<T> targetClass;

        public UnderlyingObjectJsonTrait(String fieldName, Class<T> targetClass, BiFunction<Discord, JsonNode, T> eachMapper) {
            super(null, fieldName);
            this.eachMapper = eachMapper;
            this.targetClass = targetClass;
        }

        @Override
        public JsonNode extract(JsonNode from) {
            return from.path(fieldName);
        }

        @Override
        public @Nullable T map(JsonNode node) {
            if (api == null)
                throw new IllegalStateException("API is null! Please open a bug report at " + CrystalShard.ISSUES_URL);

            return Adapter.access(targetClass, api, api, node);
        }

        @Override
        public JsonTrait<JsonNode, T> clone() {
            return new UnderlyingObjectJsonTrait<>(fieldName, targetClass, eachMapper).equalize(this);
        }
    }
}
