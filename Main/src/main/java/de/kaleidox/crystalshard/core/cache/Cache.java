package de.kaleidox.crystalshard.core.cache;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.util.annotations.NotContainNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public interface Cache<T, I, R> {
    /**
     * Request the parameters required to create a new instance after the given {@code requestIdent} object. This method must never return {@code null}.
     *
     * @param requestIdent The ident object to request parameters for.
     * @return A future that completes with an array of objects.
     */
    @NotNull
    // FIXME: 30.09.2018 Seems to not complete properly; thus blocking all #getOrRequest calls.
    CompletableFuture<Object[]> requestConstructorParameters(R requestIdent);

    /**
     * Constructs a new instance of T with the providing parameters. The provided parameters always match with the pre-defined values for the
     * defaultConstructorParameters. This method must never return {@code null}.
     *
     * @param param An array of parameters to create T from.
     * @return A new instance of T.
     */
    @NotNull
    T construct(Object... param);

    /**
     * Tries to {@linkplain #get(Object) get} an instance from the cache. If any exception is thrown, returns {@code null}.
     *
     * @param identifier The identifier to look for.
     * @return The object of {@code null}.
     * @see #get(Object)
     */
    @Nullable
    T getOrNull(I identifier);

    /**
     * Requests an instance after the given {@code ident}.
     *
     * @param ident The identifier for the object.
     * @return A future that completes with the new, requested item.
     * @throws NoSuchElementException If no reference was found for the given {@code ident}.
     */
    CompletableFuture<T> request(I ident, R requestIdent) throws NoSuchElementException;

    /**
     * Creates a new instance after the given parameters.
     *
     * @param params The parameters to pass to the {@linkplain #construct(Object...) construct} method.
     * @return The new instance.
     * @throws IllegalArgumentException If the provided parameters do not match the predefined default constructor parameters.
     */
    T getOrCreate(Object... params) throws IllegalArgumentException;

    /**
     * Gets or creates a new instance of the object, depending on whether or not it is cached.
     *
     * @param ident      The identifier for the object.
     * @param parameters An array of parameters to create the object after, if its not cached.
     * @return The object.
     * @throws IllegalArgumentException If the {@linkplain #requestConstructorParameters(Object) requested} parameters do not match the predefined default
     *                                  constructor parameters.
     */
    T getOrCreate(I ident, Object... parameters) throws IllegalArgumentException;

    /**
     * Gets or requests an instance of the object after {@code ident}, depending on whether it's cached.
     *
     * @param ident        The identifier to look for.
     * @param requestIdent The request identifier for requesting new object traits.
     * @return The object.
     * @throws NoSuchElementException   If the recent used parameters for requesting are {@code null}.
     * @throws IllegalArgumentException If the {@linkplain #requestConstructorParameters(Object) requested} parameters do not match the predefined default
     *                                  constructor parameters.
     */
    T getOrRequest(I ident, R requestIdent) throws NoSuchElementException, IllegalArgumentException;

    /**
     * Gets the instance behind the given {@code ident}.
     *
     * @param ident The identifier to look at.
     * @return The instance.
     * @throws NoSuchElementException   If there is no reference at the given identifier.
     * @throws IllegalArgumentException If the {@linkplain #requestConstructorParameters(Object) requested} parameters do not match the predefined default
     *                                  constructor parameters.
     */
    T get(I ident) throws NoSuchElementException, IllegalArgumentException;

    /**
     * Requests a set of identifiers to Cache.
     *
     * @param idents The identifiers to request.
     * @return A list of identifiers that have been successfully requested.
     */
    List<I> requestToCache(@NotContainNull I... idents);

    /**
     * Uncaches and deletes one or more elements from the cache.
     *
     * @param idents A set of identifiers to remove from the cache.
     * @return A list of idents that could be removed.
     */
    List<I> destroyFromCache(@NotContainNull I... idents);

    /**
     * Tries to get an instance of a cache for the provided parameters.
     *
     * @param typeClass The type class of the cache.
     * @param ident     The identifier for the cache.
     * @param <T>       Type variable for the CacheImpl type.
     * @param <I>       Type variable for the CacheImpl identifier.
     * @return The CacheImpl.
     * @throws NoSuchElementException If no cache fitting the parameters was found.
     */
    static <T extends Cacheable, I> Cache<T, I, ?> getCacheInstance(Class<T> typeClass, I ident) throws NoSuchElementException {
        return CoreInjector.getCacheInstance(typeClass, ident);
    }
}