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
     * Returns the cache that holds instances of this object.
     *
     * @return The cache.
     */
    Cache<T, I, R> getCache();
    
    /**
     * @param typeClass
     * @param ident
     * @param <T>
     * @param <I>
     * @return
     * @see Cache#get(I)
     */
    static <T extends Cacheable, I> T getInstance(Class<T> typeClass, I ident) {
        return Cache.getCacheInstance(typeClass, ident)
                .get(ident);
    }
    
    /**
     * @param typeClass
     * @param ident
     * @param constructorParameters
     * @param <T>
     * @param <I>
     * @return
     * @see Cache#getOrCreate(I, Object...)
     */
    static <T extends Cacheable, I> T getOrCreateInstance(Class<T> typeClass, I ident, Object... constructorParameters) {
        return Cache.getCacheInstance(typeClass, ident)
                .getOrCreate(ident, constructorParameters);
    }
}
