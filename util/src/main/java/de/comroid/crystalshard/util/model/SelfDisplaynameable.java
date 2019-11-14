package de.comroid.crystalshard.util.model;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

public interface SelfDisplaynameable<Self extends SelfDisplaynameable> {
    @SuppressWarnings("NullableProblems")
    Self withDisplayName(@NotNull String name);

    Optional<String> getDisplayName();

    @SuppressWarnings("ConstantConditions")
    default Self removeDisplayName() {
        return withDisplayName(null);
    }
}
