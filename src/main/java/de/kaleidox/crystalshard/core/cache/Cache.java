package de.kaleidox.crystalshard.core.cache;

import de.kaleidox.logging.Logger;
import de.kaleidox.util.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static de.kaleidox.util.helpers.MapHelper.*;

/**
 * This class is the basic implementation of a cache.
 *
 * @param <T> The object type that the cache contains.
 * @param <I> The type of the object that is used to uniquely identify the objects.
 * @param <R> The type of the object that is used to request a new instance of this object.
 */
public abstract class Cache<T, I, R> {
    private final static Logger                                                     logger = new Logger(Cache.class);
    private final static ConcurrentHashMap<Class<?>, Cache<Object, Object, Object>> cacheInstances;
    private final static ScheduledExecutorService                                   scheduledExecutorService;
    private final        ConcurrentHashMap<I, CacheReference<T, R>>                 instances;
    private final        Class<? extends T>                                         typeClass;
    private final        long                                                       keepaliveMilis;
    private final        Class<?>[]                                                 constructorParameter;
    private final        Constructor<? extends T>                                   constructor;
    
    // Init Blocks
    static {
        // Initialize variables
        cacheInstances = new ConcurrentHashMap<>();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Check for deletable cache references
            cacheInstances.entrySet()
                    .stream()
                    .flatMap(entry -> entry.getValue().instances.entrySet().stream())
                    .map(Map.Entry::getValue)
                    .filter(CacheReference::canBeUncached)
                    .forEachOrdered(CacheReference::uncache);
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    /**
     * Creates a new Cache instance.
     *
     * @param typeClass The constructing class for the Cache object.
     * @param keepaliveMillis The amount of milliseconds to keep each object isCached.
     * @param defaultConstructorParameter The parameter types for the constructor.
     * @param <Con> Type variable for the constructor.
     */
    public <Con extends T> Cache(Class<Con> typeClass, long keepaliveMillis, Class<?>... defaultConstructorParameter) {
        this.typeClass = typeClass;
        this.keepaliveMilis = keepaliveMillis;
        this.constructorParameter = defaultConstructorParameter;
        instances = new ConcurrentHashMap<>();
        
        try {
            this.constructor = typeClass.getConstructor(defaultConstructorParameter);
        } catch (Throwable e) {
            throw new NullPointerException(e.getMessage() + ": No constructor could be set.");
        }
        
        //noinspection unchecked
        cacheInstances.put(typeClass, (Cache<Object, Object, Object>) this);
    }
    
    /**
     * Request the parameters required to create a new instance after the given {@code requestIdent} object.
     * This method must never return {@code null}.
     *
     * @param requestIdent The ident object to request parameters for.
     * @return A future that completes with an array of objects. This array is required to test {@code TRUE} on {@link #matchingParams(Object...)}.
     */
    @NotNull
    public abstract CompletableFuture<Object[]> requestConstructorParameters(R requestIdent);
    
    /**
     * Constructs a new instance of T with the providing parameters.
     * The provided parameters always match with the pre-defined values for the defaultConstructorParameters.
     * This method must never return {@code null}.
     *
     * @param parameters An array of parameters to create T from.
     * @return A new instance of T.
     */
    @NotNull
    public abstract T construct(Object... parameters);
    
    /**
     * Requests an instance after the given {@code ident}.
     *
     * @param ident The identifier for the object.
     * @return A future that completes with the new, requested item.
     * @throws NoSuchElementException If no reference was found for the given {@code ident}.
     */
    public CompletableFuture<T> request(I ident, R requestIdent) throws NoSuchElementException {
        if (!containsKey(instances, ident))
            throw new NoSuchElementException("Element with identifyer "+ident+" has never been isCached before! " +
                                             "Try using method #getOrRequest instead.");
        CacheReference<T, R> ref = getEquals(instances, ident, null);
        if (ref.isCached()) return CompletableFuture.completedFuture(ref.getReference());
        return CompletableFuture.supplyAsync(() -> getOrRequest(ident, requestIdent == null ? ref.getRecentRequestor() : requestIdent));
    }
    
    /**
     * Gets or creates a new instance of the object, depending on whether or not it is cached.
     *
     * @param ident The identifier for the object.
     * @param parameters An array of parameters to create the object after, if its not cached.
     * @return The object.
     * @throws IllegalArgumentException If the {@linkplain #requestConstructorParameters(Object) requested} parameters do not match the predefined default constructor parameters.
     */
    public T getOrCreate(I ident, Object... parameters) throws IllegalArgumentException {
        if (containsKey(instances, ident)) {
            CacheReference<T, R> ref = getEquals(instances, ident, null);
            if (ref.isCached()) return ref.getReference();
            else {
                if (!matchingParams(parameters)) throw new IllegalArgumentException(
                        "Cannot use parameters " + Arrays.toString(parameters) + " for creating new instance with " +
                        constructor.toGenericString());
                ref.setReference(Objects.requireNonNull(construct(parameters),
                                                        "Method \"construct\" must not return null!"));
                ref.setRecentParameters(parameters);
                ref.accessed();
                logger.deeptrace(
                        "Constructed new instance with recent parameters " + Arrays.toString(parameters) + " of type " +
                        typeClass.toGenericString());
                return ref.getReference();
            }
        } else {
            if (!matchingParams(parameters)) throw new IllegalArgumentException(
                    "Cannot use parameters " + Arrays.toString(parameters) + " for creating new instance with " +
                    constructor.toGenericString());
            T val = Objects.requireNonNull(construct(parameters), "Method \"construct\" must not " + "return null!");
            logger.deeptrace(
                    "Constructed new instance with recent parameters " + Arrays.toString(parameters) + " of type " +
                    typeClass.toGenericString());
            CacheReference<T, R> ref = new CacheReference<>(val, keepaliveMilis, null, parameters);
            instances.put(ident, ref);
            return val;
        }
    }
    
    /**
     * Gets or requests an instance of the object after {@code ident}, depending on whether it's cached.
     *
     * @param ident The identifier to look for.
     * @param requestIdent The request identifier for requesting new object traits.
     * @return The object.
     * @throws NoSuchElementException If the recent used parameters for requesting are {@code null}.
     * @throws IllegalArgumentException If the {@linkplain #requestConstructorParameters(Object) requested} parameters do not match the predefined default constructor parameters.
     */
    public T getOrRequest(I ident, R requestIdent) throws NoSuchElementException, IllegalArgumentException {
        T val;
        if (containsKey(instances, ident)) {
            CacheReference<T, R> ref = getEquals(instances, ident, null);
            if (ref.isCached()) return ref.getReference();
            else {
                Object[] recentParameters = ref.getRecentParameters();
                if (recentParameters == null) throw new NoSuchElementException(
                        "Reference is not isCached and cant be created; " + "no recent parameters set.");
                if (!matchingParams(recentParameters)) throw new IllegalArgumentException(
                        "Cannot use parameters " + Arrays.toString(recentParameters) +
                        " for creating new instance with " + constructor.toGenericString());
                val = Objects.requireNonNull(construct(recentParameters),
                                             "Method \"construct\" must not " + "return null!");
                logger.deeptrace(
                        "Constructed new instance with recent parameters " + Arrays.toString(recentParameters) +
                        " of type " + typeClass.toGenericString());
                ref.setReference(val);
            }
        } else {
            Object[] params = Objects.requireNonNull(requestConstructorParameters(requestIdent)).join();
            if (!matchingParams(params)) throw new IllegalArgumentException(
                    "Cannot use parameters " + Arrays.toString(params) + " for creating new instance with " +
                    constructor.toGenericString());
            val = Objects.requireNonNull(construct(params), "Method \"construct\" must not " + "return null!");
            logger.deeptrace(
                    "Constructed new instance with recent parameters " + Arrays.toString(params) + " of type " +
                    typeClass.toGenericString());
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
     * @throws NoSuchElementException If there is no reference at the given identifier.
     * @throws IllegalArgumentException If the {@linkplain #requestConstructorParameters(Object) requested} parameters do not match the predefined default constructor parameters.
     */
    public T get(I ident) throws NoSuchElementException, IllegalArgumentException {
        T val;
        if (!containsKey(instances, ident)) throw new NoSuchElementException(
                "Instance with ident " + ident + " not found.");
        CacheReference<T, R> ref = getEquals(instances, ident, null);
        if (ref.isCached()) val = ref.getReference();
        else {
            Object[] recentParameters = ref.getRecentParameters();
            if (recentParameters == null) throw new NoSuchElementException(
                    "Reference is not isCached and cant be created; " + "no recent parameters set.");
            if (!matchingParams(recentParameters)) throw new IllegalArgumentException(
                    "Cannot use parameters " + Arrays.toString(recentParameters) + " for creating new instance with " +
                    constructor.toGenericString());
            val = Objects.requireNonNull(construct(recentParameters),
                                         "Method \"construct\" must not " + "return null!");
            logger.deeptrace("Constructed new instance with recent parameters " + Arrays.toString(recentParameters) +
                             " of type " + typeClass.toGenericString());
            ref.setReference(val);
        }
        return val;
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
