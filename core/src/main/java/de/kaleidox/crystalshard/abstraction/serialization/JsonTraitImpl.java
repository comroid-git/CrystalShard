package de.kaleidox.crystalshard.abstraction.serialization;

import java.util.function.BiFunction;
import java.util.function.Function;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

/*
implementations required:

SimpleJsonTrait(Function<JsonNode, Object> extractor, String fieldName, Function<S, T> mapper)
ComplexJsonTrait(Function<JsonNode, Object> extractor, String fieldName, BiFunction<Discord, S, T> apiMapper) 
CollectiveJsonTrait(String fieldName, Class<T extends JsonDeserializable> targetClass)
 */
public abstract class JsonTraitImpl<S, T> implements JsonTrait<S, T> {
    private final Function<JsonNode, Object> extractor;
    private final String fieldName;
    
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
    
    public static class CollectiveJsonTrait<T extends JsonDeserializable> extends JsonTraitImpl<JsonNode, T> {
        public CollectiveJsonTrait(String fieldName, Class<T> targetClass) {
            super(null, fieldName);
        }

        @Override
        public Object extract(JsonNode from) {
            return null; // todo jsonnode#path and deserialize
        }

        @Override 
        public @Nullable T map(JsonNode value) {
            return null; // todo serialize
        }
    }
}
