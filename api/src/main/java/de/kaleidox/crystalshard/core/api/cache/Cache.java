package de.kaleidox.crystalshard.core.api.cache;

import java.util.Optional;

public interface Cache<T> {
    Optional<T> getByID(long id);
}
