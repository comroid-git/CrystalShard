package de.kaleidox.crystalshard.core.cache;

/**
 * This interface marks an object as cacheable, which can then be stored within a {@link Cache}.
 *
 * @param <T> The return type for the getter method.
 * @param <I> The type that the unique identifier is in.
 * @param <R> The type of the object required to request a new instance of the object.
 */
public interface Cacheable<T extends Cacheable, I, R> {
    /**
     * @param typeClass The class that defines the type of the cache.
     * @param ident The identifier to gather from the cache.
     * @param <T> Type of the return item.
     * @param <I> Type of the identifier.
     * @return An object of T matching the given identifier.
     */
    static <T extends Cacheable, I> T getInstance(Class<T> typeClass, I ident) {
        return Cache.getCacheInstance(typeClass, ident)
                .get(ident);
    }

    /**
     * Returns the cache that holds instances of this object.
     *
     * @return The cache.
     */
    Cache<T, I, R> getCache();
}
