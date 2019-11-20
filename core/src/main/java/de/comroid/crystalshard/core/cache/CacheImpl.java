package de.comroid.crystalshard.core.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import de.comroid.crystalshard.api.entity.Snowflake;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.core.cache.CacheManagerImpl.getKeyClass;

public class CacheImpl<T extends Cacheable> implements Cache<T> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Class<T> myType;
    private final Map<Long, T> cache;
    private final Map<Long, Map<Class, Cache>> subCaches;
    private final Map<Long, Map<Class, Object>> singletonMap;

    public CacheImpl(Class<T> myType, boolean isMemberCache) {
        this.myType = myType;

        cache = new ConcurrentHashMap<>();
        subCaches = new ConcurrentHashMap<>();
        singletonMap = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<T> getByID(long id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public Optional<T> setToID(long id, T inst) {
        return Optional.ofNullable(cache.put(id, inst));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Cacheable> Optional<M> getSingleton(long baseId, Class<M> memberType) {
        return Optional.ofNullable((M) singletonMap.compute(baseId,
                (keyBaseId, valueTypeMap) -> valueTypeMap == null ? new ConcurrentHashMap<>() : valueTypeMap)
                .get(getKeyClass(memberType)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Cacheable> Optional<M> setSingleton(long baseId, Class<M> memberType, M inst) {
        if (Cacheable.getCacheInfo(inst).map(cacheInformation -> cacheInformation.type() == 1).orElse(true))
            throw new IllegalArgumentException(inst + " is not a Singleton Cacheable type!");

        return Optional.ofNullable((M) singletonMap.compute(baseId,
                (keyBaseId, valueTypeMap) -> valueTypeMap == null ? new ConcurrentHashMap<>() : valueTypeMap)
                .put(getKeyClass(memberType), inst)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Cacheable> Cache<M> getMemberCache(long baseId, Class<M> type) {
        Map<Class, Cache> subCacheMap = subCaches.compute(baseId,
                (keyBaseId, valueSubCacheMap) -> valueSubCacheMap == null
                        ? new ConcurrentHashMap<>()
                        : valueSubCacheMap);

        if (!subCacheMap.containsKey(type)) {
            CacheImpl<M> newCache = new CacheImpl<>((Class<M>) getKeyClass(type), true);
            subCacheMap.put(type, newCache);
        }

        return subCacheMap.get(type);
    }

    @Override
    public Stream<Snowflake> streamSnowflakesByID(long id) {
        @Nullable T baseCacheResult = cache.get(id);

        @SuppressWarnings("unchecked")
        Stream<Snowflake> subCacheResults = subCaches.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .flatMap(cache -> cache.streamSnowflakesByID(id))
                .filter(Snowflake.class::isInstance)
                .filter(flake -> ((Snowflake) flake).getID() == id);

        Stream<Snowflake> singletonCacheResults = singletonMap.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .map(Snowflake.class::cast)
                .filter(flake -> flake.getID() == id);

        return Stream.concat(Stream.of((Snowflake) baseCacheResult), Stream.concat(subCacheResults, singletonCacheResults))
                .filter(Objects::nonNull);
    }

    @Override
    public void delete(long id) {
        cache.remove(id);
    }

    @Override
    public void close() {
        cache.clear();
        subCaches.clear();
    }
}
