package de.kaleidox.crystalshard.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.core.api.cache.Cache;
import de.kaleidox.crystalshard.core.api.cache.Cacheable;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Nullable;

import static de.kaleidox.crystalshard.core.cache.CacheManagerImpl.getKeyClass;

public class CacheImpl<T extends Cacheable & Snowflake> implements Cache<T> {
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
    public void delete(long id) {
        cache.remove(id);
    }

    @Override
    public void close() {
        cache.clear();
        subCaches.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Cacheable & Snowflake> Optional<M> getSingleton(long baseId, Class<M> memberType) {
        return Optional.ofNullable((M) singletonMap.compute(baseId,
                (keyBaseId, valueTypeMap) -> valueTypeMap == null ? new ConcurrentHashMap<>() : valueTypeMap)
                .get(getKeyClass(memberType)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Cacheable & Snowflake> Optional<M> setSingleton(long baseId, Class<M> memberType, M inst) {
        if (!inst.isSingletonType())
            throw new IllegalArgumentException(inst+" is not a Singleton Cacheable type!");

        return Optional.ofNullable((M) singletonMap.compute(baseId,
                (keyBaseId, valueTypeMap) -> valueTypeMap == null ? new ConcurrentHashMap<>() : valueTypeMap)
                .put(getKeyClass(memberType), inst)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Cacheable & Snowflake> Cache<M> getMemberCache(long baseId, Class<M> type) {
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
    public Collection<Snowflake> getSnowflakesByID(long id) {
        @Nullable Snowflake baseCacheResult = cache.get(id);

        List<Snowflake> subCacheResults = subCaches.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .map(cache -> cache.getSnowflakesByID(id))
                .flatMap(Collection::stream)
                .map(Snowflake.class::cast)
                .filter(flake -> flake.getID() == id)
                .collect(Collectors.toList());

        List<Snowflake> singletonCacheResults = singletonMap.values()
                .stream()
                .flatMap(map -> map.values().stream())
                .map(Snowflake.class::cast)
                .filter(flake -> flake.getID() == id)
                .collect(Collectors.toList());

        Collection<Snowflake> yields = new ArrayList<>();
        if (baseCacheResult != null) yields.add(baseCacheResult);
        yields.addAll(subCacheResults);
        yields.addAll(singletonCacheResults);

        return Collections.unmodifiableCollection(yields);
    }
}
