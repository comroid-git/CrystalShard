package de.kaleidox.crystalshard.util.model.serialization;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.kaleidox.crystalshard.CrystalShard;
import de.kaleidox.crystalshard.api.entity.Snowflake;

import org.jetbrains.annotations.Nullable;

public interface JsonDeserializable {
    default Set<JsonTrait> possibleTraits() {
        JsonTraits traitsClass = getClass().getAnnotation(JsonTraits.class);

        if (traitsClass == null)
            throw new AssertionError("Could not determine @JsonTraits annotation for "
                    + getClass().getSimpleName() + "! Please open an issue at " + CrystalShard.ISSUES_URL);

        return Arrays.stream(traitsClass.value()
                .getFields())
                .filter(field -> Snowflake.Trait.class.isAssignableFrom(field.getType()))
                .filter(field -> Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
                .map(field -> {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new AssertionError("Could not access Traits", e);
                    }
                })
                .map(JsonTrait.class::cast)
                .collect(Collectors.toSet());
    }

    <S, T> @Nullable T getTraitValue(JsonTrait<S, T> trait);

    default <T> Optional<T> wrapTraitValue(JsonTrait<?, T> trait) {
        return Optional.ofNullable(getTraitValue(trait));
    }
}
