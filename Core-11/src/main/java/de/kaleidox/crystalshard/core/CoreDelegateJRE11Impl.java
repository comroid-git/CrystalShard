package de.kaleidox.crystalshard.core;

import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.cache.sub.*;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.net.request.DiscordRequestImpl;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.WebRequestImpl;
import de.kaleidox.crystalshard.core.net.request.ratelimit.RatelimiterImpl;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiter;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClient;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClientImpl;
import de.kaleidox.crystalshard.main.Discord;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;

public class CoreDelegateJRE11Impl extends CoreDelegate {
    private final static Hashtable<Class, Class> implementations;

    static {
        implementations = new Hashtable<>();
        implementations.put(Cache.class, CacheImpl.class);
        implementations.put(ThreadPool.class, ThreadPoolImpl.class);
        implementations.put(WebRequest.class, WebRequestImpl.class);
        implementations.put(Ratelimiter.class, RatelimiterImpl.class);
        implementations.put(WebSocketClient.class, WebSocketClientImpl.class);
    }

    public CoreDelegateJRE11Impl() {
        super(implementations);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <I, T extends Cacheable> Cache<T, I, ?> getCacheInstanceDelegate(Class<T> typeClass, I ident) {
        return CacheImpl.cacheInstances.entrySet()
                .stream()
                .filter(entry -> typeClass.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .filter(cache -> {
                    try {
                        CacheImpl<T, I, ?> o = (CacheImpl<T, I, ?>) cache;
                    } catch (Throwable e) {
                        return false;
                    }
                    return true;
                })
                .map(cache -> (CacheImpl<T, I, ?>) cache)
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    protected <T> WebRequest<T> makeWebRequest(Discord discord) {
        return new DiscordRequestImpl<>(discord);
    }

    @Override
    protected ChannelCacheImpl makeChannelCache(Discord discord) {
        return new ChannelCacheImpl(discord);
    }

    @Override
    protected EmojiCacheImpl makeEmojiCache(Discord discord) {
        return new EmojiCacheImpl(discord);
    }

    @Override
    protected MessageCacheImpl makeMessageCache(Discord discord) {
        return new MessageCacheImpl(discord);
    }

    @Override
    protected RoleCacheImpl makeRoleCache(Discord discord) {
        return new RoleCacheImpl(discord);
    }

    @Override
    protected ServerCacheImpl makeServerCache(Discord discord) {
        return new ServerCacheImpl(discord);
    }

    @Override
    protected UserCacheImpl makeUserCache(Discord discord) {
        return new UserCacheImpl(discord);
    }

    @Override
    public int getJdkVersion() {
        return 11;
    }
}
