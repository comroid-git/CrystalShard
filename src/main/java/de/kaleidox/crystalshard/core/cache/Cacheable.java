package de.kaleidox.crystalshard.core.cache;

/**
 * This interface marks an object as cacheable, which can then be stored within a {@link Cache}.
 *
 * @param <T>         The return type for the getter method.
 * @param <I>        The type that the unique identifier is in.
 * @param <R> The type of the object required to request a new instance of the object.
 */
public interface Cacheable<T, I, R> {
    /**
     * Returns the cache that holds instances of this object.
     *
     * @return The cache.
     */
    Cache<T, I, R> getCache();
}
