package de.kaleidox.crystalshard.util.model.serialization;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.kaleidox.crystalshard.CrystalShard;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.Snowflake;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

public interface JsonDeserializable extends Cloneable {
    Set<JsonTrait> possibleTraits();

    <S, T> @Nullable T getTraitValue(JsonTrait<S, T> trait);

    default <T> Optional<T> wrapTraitValue(JsonTrait<?, T> trait) {
        return Optional.ofNullable(getTraitValue(trait));
    }
    
    void updateFromJson(final JsonNode data);
}
