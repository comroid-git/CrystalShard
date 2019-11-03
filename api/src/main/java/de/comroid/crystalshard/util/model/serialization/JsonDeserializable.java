package de.comroid.crystalshard.util.model.serialization;

import java.util.Optional;
import java.util.Set;

import de.comroid.crystalshard.api.model.ApiBound;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

public interface JsonDeserializable extends ApiBound, Cloneable {
    Set<JsonBinding> possibleTraits();

    <S, T> @Nullable T getTraitValue(JsonBinding<?, S, ?, T> trait);

    default <T> Optional<T> wrapTraitValue(JsonBinding<?, ?, ?, T> trait) {
        return Optional.ofNullable(getTraitValue(trait));
    }
    
    Set<JsonBinding> updateFromJson(final JSONObject data);
}
