package org.comroid.crystalshard.core.cache;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.comroid.crystalshard.abstraction.AbstractApiBound;
import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.entity.Snowflake;

import com.google.common.flogger.FluentLogger;

@SuppressWarnings("unchecked")
public class CacheManagerImpl extends AbstractApiBound implements CacheManager {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Map<Class, CacheImpl<?>> caches;

    public CacheManagerImpl(Discord api) {
        super(api);

        caches = new ConcurrentHashMap<>();
    }

    @Override
    public <R extends Cacheable> Optional<R> set(Class<R> type, long id, R instance) {
        if (Cacheable.getCacheInfo(instance).map(it -> true).orElse(true))
            throw new IllegalArgumentException(instance + " cannot be stored in a parentcache!");

        return getCache(type).setToID(id, instance);
    }

    @Override
    public <M extends Cacheable, B extends Cacheable> Optional<M> setMember(Class<B> baseType, Class<M> memberType, long baseId, long memberId, M instance) {
        if (Cacheable.getCacheInfo(instance).map(cacheInformation -> cacheInformation.type() == 1).orElse(true))
            throw new IllegalArgumentException(instance + " is not a Member Cacheable type!");

        return getCache(baseType)
                .getMemberCache(baseId, memberType)
                .setToID(memberId, instance);
    }

    @Override
    public <M extends Cacheable, B extends Cacheable> Optional<M> setSingleton(Class<B> baseType, Class<M> memberType, long baseId, M instance) {
        if (Cacheable.getCacheInfo(instance).map(cacheInformation -> cacheInformation.type() == 0).orElse(true))
            throw new IllegalArgumentException(instance + " is not a Singleton Cacheable type!");

        return getCache(baseType)
                .setSingleton(baseId, memberType, instance);
    }

    @Override
    public <R extends Cacheable> Void delete(Class<R> type, long id) {
        getCache(type).delete(id);

        return null;
    }

    @Override
    public <B extends Cacheable, M extends Cacheable> Void deleteMember(Class<B> baseType, Class<M> memberType, long baseId, long memberId) {
        getCache(baseType)
                .getMemberCache(baseId, memberType)
                .delete(memberId);

        return null;
    }

    @Override
    public <R extends Cacheable> Cache<R> getCache(Class<R> forType) {
        return (Cache<R>) caches.compute(getKeyClass(forType),
                (k, v) -> v == null ? new CacheImpl<>(forType, false) : v);
    }

    @Override
    public Stream<Snowflake> streamSnowflakesByID(long id) {
        return caches.values()
                .stream()
                .flatMap(cache -> cache.streamSnowflakesByID(id))
                .parallel();
    }

    // todo Inspect behavior of this
    static Class<? extends Cacheable> getKeyClass(Class<?> forType) {
        Class<?>[] interfaces = forType.getInterfaces();

        for (Class<?> anInterface : interfaces) {
            if (anInterface.getPackageName().contains("org.comroid.crystalshard.api.")
                    && Arrays.asList(anInterface.getInterfaces()).contains(Cacheable.class)) {
                return (Class<? extends Cacheable>) anInterface;
            }
        }

        for (Class<?> itf : interfaces) {
            Class<? extends Cacheable> aClass = getKeyClass(itf);

            if (aClass != null)
                return aClass;
        }

        return null;
    }
}
