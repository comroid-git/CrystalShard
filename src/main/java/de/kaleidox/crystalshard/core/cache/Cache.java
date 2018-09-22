package de.kaleidox.crystalshard.core.cache;

import de.kaleidox.logging.Logger;
import de.kaleidox.util.helpers.MapHelper;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class Cache<Type, Ident> {
    private final static Logger                                         logger = new Logger(Cache.class);
    private final static ConcurrentHashMap<Class<?>, Cache<?, ?>>       cacheInstances;
    private final static ScheduledExecutorService                       scheduledExecutorService;
    private final        ConcurrentHashMap<Ident, CacheReference<Type>> instances;
    private final        Class<? extends Type>                          typeClass;
    private final        long                                           keepaliveMilis;
    private final        Class<?>[]                                     constructorParameter;
    private final        Constructor<? extends Type>                    constructor;
    
// Init Blocks
    static {
        cacheInstances = new ConcurrentHashMap<>();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Check for deletable cachereferences
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    public <Con extends Type> Cache(Class<Con> typeClass, long keepaliveMilis, Class<?>... constructorParameter) {
        this.typeClass = typeClass;
        this.keepaliveMilis = keepaliveMilis;
        this.constructorParameter = constructorParameter;
        instances = new ConcurrentHashMap<>();
        
        try {
            this.constructor = typeClass.getConstructor(constructorParameter);
        } catch (Throwable e) {
            throw new NullPointerException(e.getMessage() + ": No constructor could be set.");
        }
        
        cacheInstances.put(typeClass, this);
    }
    
    public abstract CompletableFuture<Type> request(Ident ident);
    
    public abstract Type get(Ident ident);
    
    public abstract Type getOrCreate(Ident ident, Object... defaultConstructorValues);
    
    public abstract Type construct(Object... parameters);
    
    public final boolean matchingParams(Object... parameter) {
        boolean match = true;
        
        for (int i = 0; i < parameter.length; i++) {
            if (!constructorParameter[i].isAssignableFrom(parameter[i].getClass())) match = false;
        }
        
        return match;
    }
    
// Static members
    public final static <sType, sIdent, C extends Cache<sType, sIdent>> C getCacheInstance(Class<sType> typeClass,
                                                                                           Class<sIdent> identClass,
                                                                                           Supplier<C> cacheIfAbsent) {
        /*return MapHelper.getSpecialComparator(
                cacheInstances,
                typeClass,
                cacheIfAbsent,
                clazz -> typeClass.isAssignableFrom((Class<?>) clazz)
        );*/
        return null;
    }
}
