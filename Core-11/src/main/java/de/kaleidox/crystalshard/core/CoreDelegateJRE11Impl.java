package de.kaleidox.crystalshard.core;

import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.cache.sub.ChannelCacheImpl;
import de.kaleidox.crystalshard.core.cache.sub.EmojiCacheImpl;
import de.kaleidox.crystalshard.core.cache.sub.MessageCacheImpl;
import de.kaleidox.crystalshard.core.cache.sub.RoleCacheImpl;
import de.kaleidox.crystalshard.core.cache.sub.ServerCacheImpl;
import de.kaleidox.crystalshard.core.cache.sub.UserCacheImpl;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.WebRequestImpl;
import de.kaleidox.crystalshard.main.Discord;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class CoreDelegateJRE11Impl extends CoreDelegate {
    @SuppressWarnings("unchecked")
    @Override
    protected <T> T makeInstance(Class<T> tClass, Object... args) {
        try {
            Class[] types = Stream.of(args).map(Object::getClass).toArray(Class[]::new);
            switch (tClass.getSimpleName()) {
                case "ThreadPool":
                    return (T) ThreadPoolImpl.class.getConstructor(types).newInstance(args);
                case "WebRequest":
                    return (T) WebRequest.class.getConstructor(types).newInstance(args);
            }
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
        throw new NoSuchElementException("Cannot create instance of "+tClass.getName()+"!");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected <I, T extends Cacheable> Cache<T, I, ?> getCacheInstanceDelegate(Class<T> typeClass, I ident) {
        return CacheImpl.cacheInstances.entrySet().stream().filter(entry -> typeClass.isAssignableFrom(entry.getKey())).map(Map.Entry::getValue).filter(cache -> {
            try {
                CacheImpl<T, I, ?> o = (CacheImpl<T, I, ?>) cache;
            } catch (Throwable e) {
                return false;
            }
            return true;
        }).map(cache -> (CacheImpl<T, I, ?>) cache).findAny().orElseThrow(NoSuchElementException::new);
    }
    
    @Override
    protected <T> WebRequest<T> makeWebRequest(Discord discord) {
        return new WebRequestImpl<>(discord);
    }
    
    protected ChannelCacheImpl makeChannelCache(Discord discord) {
        return new ChannelCacheImpl(discord);
    }
    
    protected EmojiCacheImpl makeEmojiCache(Discord discord) {
        return new EmojiCacheImpl(discord);
    }
    
    protected MessageCacheImpl makeMessageCache(Discord discord) {
        return new MessageCacheImpl(discord);
    }
    
    protected RoleCacheImpl makeRoleCache(Discord discord) {
        return new RoleCacheImpl(discord);
    }
    
    protected ServerCacheImpl makeServerCache(Discord discord) {
        return new ServerCacheImpl(discord);
    }
    
    protected UserCacheImpl makeUserCache(Discord discord) {
        return new UserCacheImpl(discord);
    }
}
