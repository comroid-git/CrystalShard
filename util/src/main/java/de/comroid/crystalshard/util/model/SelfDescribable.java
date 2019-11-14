package de.comroid.crystalshard.util.model;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

public interface SelfDescribable<Self extends SelfDescribable> {
    @SuppressWarnings("NullableProblems")
    Self withDescription(@NotNull String description);

    Optional<String> getDescription();

    @SuppressWarnings("ConstantConditions")
    default Self removeDescription() {
        return withDescription(null);
    }
}
