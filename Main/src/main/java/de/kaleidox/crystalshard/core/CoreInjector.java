package de.kaleidox.crystalshard.core;

import de.kaleidox.crystalshard.InjectorBase;
import de.kaleidox.crystalshard.core.cache.Cache;
import de.kaleidox.crystalshard.core.cache.Cacheable;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiter;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClient;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.objects.markers.IDPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;

public abstract class CoreInjector extends InjectorBase {
    public final static CoreInjector delegate;
    private final static Set<Class> mustOverride;

    static {
        CoreInjector using;
        ServiceLoader<CoreInjector> load = ServiceLoader.load(CoreInjector.class);
        Iterator<CoreInjector> iterator = load.iterator();
        if (iterator.hasNext()) using = iterator.next();
        else throw new IllegalStateException("No implementation for " + CoreInjector.class.getName() + " found!");
        if (iterator.hasNext()) {
            List<CoreInjector> allImplementations = new ArrayList<>();
            allImplementations.add(using);
            iterator.forEachRemaining(allImplementations::add);
            allImplementations.sort(Comparator.comparingInt(delegate -> delegate.getJdkVersion() * -1));
            using = allImplementations.get(0);
            logger.warn("More than one implementation for " + CoreInjector.class.getSimpleName() +
                    " found! Using " + using.getClass().getName());
        }
        delegate = using;
        mustOverride = new HashSet<>();
        mustOverride.addAll(Arrays.asList(
                Cache.class,
                ThreadPool.class,
                WebRequest.class,
                Ratelimiter.class,
                WebSocketClient.class));
    }

    public CoreInjector(Hashtable<Class, Class> implementations) {
        super(implementations, mustOverride);
    }

    public static Cache<Channel, Long, Long> channelCache(Discord discord) {
        return delegate.makeChannelCache(discord);
    }

    protected abstract Cache<Channel, Long, Long> makeChannelCache(Discord discord);

    public static Cache<CustomEmoji, Long, IDPair> emojiCache(Discord discord) {
        return delegate.makeEmojiCache(discord);
    }

    protected abstract Cache<CustomEmoji, Long, IDPair> makeEmojiCache(Discord discord);

    public static Cache<Message, Long, IDPair> messageCache(Discord discord) {
        return delegate.makeMessageCache(discord);
    }

    protected abstract Cache<Message, Long, IDPair> makeMessageCache(Discord discord);

    public static Cache<Role, Long, IDPair> roleCache(Discord discord) {
        return delegate.makeRoleCache(discord);
    }

    protected abstract Cache<Role, Long, IDPair> makeRoleCache(Discord discord);

    public static Cache<Server, Long, Long> serverCache(Discord discord) {
        return delegate.makeServerCache(discord);
    }

    protected abstract Cache<Server, Long, Long> makeServerCache(Discord discord);

    public static Cache<User, Long, Long> userCache(Discord discord) {
        return delegate.makeUserCache(discord);
    }

    protected abstract Cache<User, Long, Long> makeUserCache(Discord discord);

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

    protected abstract <I, T extends Cacheable> Cache<T, I, ?> getCacheInstanceDelegate(Class<T> typeClass, I ident);

    public static <T> WebRequest<T> webRequest(Class<T> tClass, Discord discord) {
        WebRequest<T> request = webRequest(discord);
        return request;
    }

    public static <T> WebRequest<T> webRequest(Discord discord) {
        return delegate.makeWebRequest(discord);
    }

    protected abstract <T> WebRequest<T> makeWebRequest(Discord discord);
}
