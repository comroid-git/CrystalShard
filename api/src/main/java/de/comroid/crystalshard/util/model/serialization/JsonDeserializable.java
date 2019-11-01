package de.comroid.crystalshard.util.model.serialization;

import java.util.Optional;
import java.util.Set;

import de.comroid.crystalshard.api.model.ApiBound;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

public interface JsonDeserializable extends ApiBound, Cloneable {
    Set<JsonTrait> possibleTraits();

    <S, T> @Nullable T getTraitValue(JsonTrait<S, T> trait);

    default <T> Optional<T> wrapTraitValue(JsonTrait<?, T> trait) {
        return Optional.ofNullable(getTraitValue(trait));
    }
    
    void updateFromJson(final JsonNode data);
}
