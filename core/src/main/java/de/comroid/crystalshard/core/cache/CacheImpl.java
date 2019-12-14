package de.comroid.crystalshard.core.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

import de.comroid.crystalshard.api.entity.Snowflake;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.core.cache.CacheManagerImpl.getKeyClass;

@SuppressWarnings("rawtypes")
public class CacheImpl<T extends Cacheable> implements Cache<T> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Map<Long, Semaphore> semaphores = new ConcurrentHashMap<>();

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
        return Optional.ofNullable(getImpl(id));
    }

    @Override
    public Optional<T> setToID(long id, T inst) {
        return Optional.ofNullable(setImpl(id, inst));
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
            CacheImpl<M> newCache = new CacheImpl<>((Class<M>) Objects.requireNonNull(getKeyClass(type)), true);
            subCacheMap.put(type, newCache);
        }

        return subCacheMap.get(type);
    }

    @Override
    public Stream<Snowflake> streamSnowflakesByID(long id) {
        @Nullable T baseCacheResult = getImpl(id);

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
        final Semaphore available = semaphore(id);

        //noinspection SynchronizationOnLocalVariableOrMethodParameter -> semaphore is effectively final per id
        synchronized (available) {
            available.acquireUninterruptibly(1);

            cache.remove(id);
            subCaches.remove(id);
            singletonMap.remove(id);

            available.release(1);
        }
    }

    @Override
    public void close() {
        cache.clear();
        subCaches.clear();
    }

    protected final @Nullable T setImpl(long id, T value) {
        if (myType.isInstance(value))
            throw new IllegalArgumentException(String.format("Cannot place type %s in Cache<%s>", value.getClass().getName(), myType.getSimpleName()));

        final Semaphore available = semaphore(id);

        //noinspection SynchronizationOnLocalVariableOrMethodParameter -> semaphore is effectively final per id
        synchronized (available) {
            available.acquireUninterruptibly(1);

            final T old = cache.put(id, value);

            available.release(1);

            return old;
        }
    }

    protected final @Nullable T getImpl(long id) {
        final Semaphore available = semaphore(id);

        //noinspection SynchronizationOnLocalVariableOrMethodParameter -> semaphore is effectively final per id
        synchronized (available) {
            available.acquireUninterruptibly(1);

            final T value = cache.getOrDefault(id, null);

            available.release(1);

            return value;
        }
    }

    @Contract(pure = true)
    protected final Semaphore semaphore(long id) {
        final Semaphore semaphore = semaphores.computeIfAbsent(id, key -> new Semaphore(1, true));

        if (semaphore.availablePermits() > 1)
            throw new IllegalStateException("Only one permit allowed per Cache Semaphore");

        return semaphore;
    }
}
