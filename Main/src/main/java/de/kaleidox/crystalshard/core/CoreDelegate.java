package de.kaleidox.crystalshard.core;

import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.main.Discord;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

public abstract class CoreDelegate {
    public final static CoreDelegate delegate;
    
    static {
        ServiceLoader<CoreDelegate> load = ServiceLoader.load(CoreDelegate.class);
        Iterator<CoreDelegate> iterator = load.iterator();
        if (iterator.hasNext()) delegate = iterator.next();
        else throw new IllegalStateException("No implementation for " + CoreDelegate.class.getName() + " found!");
        if (iterator.hasNext()) throw new IllegalStateException("More than one implementation for " + CoreDelegate.class.getName() + " found!");
    }
    
    protected abstract <T> T makeInstance(Class<T> tClass, Object... args);
    
    public static <T> T newInstance(Class<T> tClass, Object... args) {
        return delegate.makeInstance(tClass, args);
    }
    
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
    @SuppressWarnings("unchecked")
    public static <T extends Cacheable, I> Cache<T, I, ?> getCacheInstance(Class<T> typeClass, I ident) throws NoSuchElementException {
        return delegate.getCacheInstanceDelegate(typeClass, ident);
    }
    
    protected abstract <I, T extends Cacheable> Cache<T,I,?> getCacheInstanceDelegate(Class<T> typeClass, I ident);
    
    public static <T> WebRequest<T> webRequest(Discord discord) {
        return delegate.makeWebRequest(discord);
    }
    
    protected abstract <T> WebRequest<T> makeWebRequest(Discord discord);
}
