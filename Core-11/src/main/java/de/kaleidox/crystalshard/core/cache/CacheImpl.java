package de.kaleidox.crystalshard.core.cache;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.util.annotations.NotContainNull;
import de.kaleidox.crystalshard.util.annotations.NotNull;
import de.kaleidox.crystalshard.util.annotations.Nullable;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

import static de.kaleidox.crystalshard.util.helpers.MapHelper.containsKey;
import static de.kaleidox.crystalshard.util.helpers.MapHelper.getEquals;

/**
 * This class is the basic implementation of a cache.
 *
 * @param <T> The object type that the cache contains.
 * @param <I> The type of the object that is used to uniquely identify the objects.
 * @param <R> The type of the object that is used to request a new instance of this object.
 */
public abstract class CacheImpl<T extends Cacheable, I, R> implements Cache<T, I, R> {
    public final static ConcurrentHashMap<Class<?>, CacheImpl<? extends Cacheable, Object, Object>> cacheInstances;
    private final static Logger logger = new Logger(CacheImpl.class);
    private final static ScheduledExecutorService scheduledExecutorService;

    static {
        // Initialize variables
        cacheInstances = new ConcurrentHashMap<>();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Check for deletable cache references
            cacheInstances.entrySet()
                    .stream()
                    .flatMap(entry -> entry.getValue().instances.entrySet()
                            .stream())
                    .map(Map.Entry::getValue)
                    .filter(CacheReference::canBeUncached)
                    .forEachOrdered(CacheReference::uncache);
        }, 30, 30, TimeUnit.SECONDS);
    }

    private final ConcurrentHashMap<I, CacheReference<T, R>> instances;
    private final Class<? extends T> typeClass;
    private final Function<Object[], I> mapperToIdentifier;
    private final long keepaliveMilis;
    private final Class<?>[] constructorParameter;

    /**
     * Creates a new CacheImpl instance.
     *
     * @param <Con>                       Type variable for the constructor.
     * @param typeClass                   The constructing class for the CacheImpl object.
     * @param mapperToIdentifier          A mapper to extract an identifier from constructor parameters.
     * @param keepaliveMillis             The amount of milliseconds to keep each object isCached.
     * @param defaultConstructorParameter The parameter types for the constructor.
     */
    public <Con extends T> CacheImpl(Class<Con> typeClass, Function<Object[], I> mapperToIdentifier, long keepaliveMillis,
                                     Class<?>... defaultConstructorParameter) {
        this.typeClass = typeClass;
        this.mapperToIdentifier = mapperToIdentifier;
        this.keepaliveMilis = keepaliveMillis;
        this.constructorParameter = defaultConstructorParameter;
        instances = new ConcurrentHashMap<>();

        //noinspection unchecked
        cacheInstances.put(typeClass, (CacheImpl<? extends Cacheable, Object, Object>) this);
    }

    @NotNull // FIXME: 30.09.2018 Seems to not complete properly; thus blocking all #getOrRequest calls.
    public abstract CompletableFuture<Object[]> requestConstructorParameters(R requestIdent);

    @NotNull
    public abstract T construct(Object... param);

    @Override
    @Nullable
    public T getOrNull(I identifier) {
        try {
            return get(identifier);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public CompletableFuture<T> request(I ident, R requestIdent) throws NoSuchElementException {
        if (!containsKey(instances, ident)) throw new NoSuchElementException(
                "Element with identifyer " + ident + " has never been isCached before! " + "Try using method #getOrRequest instead.");
        CacheReference<T, R> ref = getEquals(instances, ident, null);
        if (ref.isCached()) return CompletableFuture.completedFuture(ref.getReference());
        return CompletableFuture.supplyAsync(() -> getOrRequest(ident, requestIdent == null ? ref.getRecentRequestor() : requestIdent));
    }

    @Override
    public T getOrCreate(Object... params) throws IllegalArgumentException {
        if (!matchingParams(params)) throw new IllegalArgumentException(
                "Cannot use parameters " + Arrays.toString(params) + " for creating new instance of " + typeClass.toGenericString());
        I ident = mapperToIdentifier.apply(params);
        if (containsKey(instances, ident)) {
            CacheReference<T, R> ref = getEquals(instances, ident, null);
            if (ref.isCached()) return ref.getReference();
            else {
                T val = Objects.requireNonNull(construct(params), "Method \"construct\" must not " + "return null!");
                logger.deeptrace("Constructed new instance with passed parameters " + Arrays.toString(params) + " of type " + typeClass.toGenericString());
                ref.setReference(val);
                return val;
            }
        } else {
            T val = Objects.requireNonNull(construct(params), "Method \"construct\" must not " + "return null!");
            logger.deeptrace("Constructed new instance with passed parameters " + Arrays.toString(params) + " of type " + typeClass.toGenericString());
            CacheReference<T, R> ref = new CacheReference<>(val, keepaliveMilis, null, params);
            instances.put(ident, ref);
            return val;
        }
    }

    @Override
    public T getOrCreate(I ident, Object... parameters) throws IllegalArgumentException {
        if (containsKey(instances, ident)) {
            CacheReference<T, R> ref = getEquals(instances, ident, null);
            if (ref.isCached()) return ref.getReference();
            else {
                if (!matchingParams(parameters)) throw new IllegalArgumentException(
                        "Cannot use parameters " + Arrays.toString(parameters) + " for creating new instance of " + typeClass.toGenericString());
                ref.setReference(Objects.requireNonNull(construct(parameters), "Method \"construct\" must not return null!"));
                ref.setRecentParameters(parameters);
                ref.accessed();
                logger.deeptrace("Constructed new instance with recent parameters " + Arrays.toString(parameters) + " of type " + typeClass.toGenericString());
                return ref.getReference();
            }
        } else {
            if (!matchingParams(parameters)) throw new IllegalArgumentException(
                    "Cannot use parameters " + Arrays.toString(parameters) + " for creating new instance of " + typeClass.toGenericString());
            T val = Objects.requireNonNull(construct(parameters), "Method \"construct\" must not " + "return null!");
            logger.deeptrace("Constructed new instance with recent parameters " + Arrays.toString(parameters) + " of type " + typeClass.toGenericString());
            CacheReference<T, R> ref = new CacheReference<>(val, keepaliveMilis, null, parameters);
            instances.put(ident, ref);
            return val;
        }
    }

    @Override
    public T getOrRequest(I ident, R requestIdent) throws NoSuchElementException, IllegalArgumentException {
        T val;
        if (containsKey(instances, ident)) {
            CacheReference<T, R> ref = getEquals(instances, ident, null);
            if (ref.isCached()) return ref.getReference();
            else {
                Object[] recentParameters = ref.getRecentParameters();
                if (recentParameters == null)
                    throw new NoSuchElementException("Reference is not cached and cant be created; " + "no recent parameters set.");
                if (!matchingParams(recentParameters)) throw new IllegalArgumentException(
                        "Cannot use parameters " + Arrays.toString(recentParameters) + " for creating new instance of " + typeClass.toGenericString());
                val = Objects.requireNonNull(construct(recentParameters), "Method \"construct\" must not " + "return null!");
                logger.deeptrace(
                        "Constructed new instance with recent parameters " + Arrays.toString(recentParameters) + " of type " + typeClass.toGenericString());
                ref.setReference(val);
            }
        } else {
            Object[] params = Objects.requireNonNull(requestConstructorParameters(requestIdent))
                    .join();
            if (!matchingParams(params)) throw new IllegalArgumentException(
                    "Cannot use parameters " + Arrays.toString(params) + " for creating new instance of " + typeClass.toGenericString());
            val = Objects.requireNonNull(construct(params), "Method \"construct\" must not " + "return null!");
            logger.deeptrace("Constructed new instance with recent parameters " + Arrays.toString(params) + " of type " + typeClass.toGenericString());
            CacheReference<T, R> ref = new CacheReference<>(val, keepaliveMilis, requestIdent, params);
            instances.put(ident, ref);
        }
        return val;
    }

    /**
     * Gets the instance behind the given {@code ident}.
     *
     * @param ident The identifier to look at.
     * @return The instance.
     * @throws NoSuchElementException   If there is no reference at the given identifier.
     * @throws IllegalArgumentException If the {@linkplain #requestConstructorParameters(Object) requested} parameters do not match the predefined default
     *                                  constructor parameters.
     */
    public T get(I ident) throws NoSuchElementException, IllegalArgumentException {
        T val;
        if (!containsKey(instances, ident))
            throw new NoSuchElementException("Instance with ident " + ident + " not found.");
        CacheReference<T, R> ref = getEquals(instances, ident, null);
        if (ref.isCached()) val = ref.getReference();
        else {
            Object[] recentParameters = ref.getRecentParameters();
            if (recentParameters == null)
                throw new NoSuchElementException("Reference is not isCached and cant be created; " + "no recent parameters set.");
            if (!matchingParams(recentParameters)) throw new IllegalArgumentException(
                    "Cannot use parameters " + Arrays.toString(recentParameters) + " for creating new instance of " + typeClass.toGenericString());
            val = Objects.requireNonNull(construct(recentParameters), "Method \"construct\" must not " + "return null!");
            logger.deeptrace(
                    "Constructed new instance with recent parameters " + Arrays.toString(recentParameters) + " of type " + typeClass.toGenericString());
            ref.setReference(val);
        }
        return val;
    }

    @SafeVarargs
    public final List<I> requestToCache(@NotContainNull I... idents) {
        List<I> list = new ArrayList<>();

        for (I ident : idents) {
            Objects.requireNonNull(ident);
            instances.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey()
                            .equals(ident))
                    .forEachOrdered(entry -> {
                        try {
                            CacheReference<T, R> ref = entry.getValue();
                            Object[] recentParameters = ref.getRecentParameters();
                            if (recentParameters == null) throw new NoSuchElementException(
                                    "Reference is not isCached and cant be created; " + "no recent parameters set.");
                            if (!matchingParams(recentParameters)) throw new IllegalArgumentException(
                                    "Cannot use parameters " + Arrays.toString(recentParameters) + " for creating new instance of " + typeClass.toGenericString());
                            ref.setReference(Objects.requireNonNull(construct(recentParameters), "Method \"construct\" must not " + "return null!"));
                            logger.deeptrace(
                                    "Constructed new instance with recent parameters " + Arrays.toString(recentParameters) + " of type " + typeClass.toGenericString());
                        } finally {
                            list.add(ident);
                        }
                    });
        }

        return list;
    }

    @SafeVarargs
    public final List<I> destroyFromCache(@NotContainNull I... idents) {
        List<I> list = new ArrayList<>();

        for (I ident : idents) {
            Objects.requireNonNull(ident);
            instances.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey()
                            .equals(ident))
                    .forEachOrdered(entry -> {
                        try {
                            CacheReference<T, R> ref = entry.getValue();
                            instances.remove(entry.getKey(), ref);
                            ref.uncache();
                            ref.close();
                        } finally {
                            list.add(ident);
                        }
                    });
        }

        return list;
    }

    /**
     * Checks whether the passed array of objects can fit into the default constructor parameters.
     *
     * @param parameter An array of parameters.
     * @return Whether the parameters can fit.
     */
    private boolean matchingParams(Object... parameter) {
        boolean match = true;

        for (int i = 0; i < parameter.length; i++) {
            if (!constructorParameter[i].isAssignableFrom(parameter[i].getClass())) match = false;
        }

        return match;
    }
}
