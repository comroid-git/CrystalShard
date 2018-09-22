package de.kaleidox.crystalshard.core.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.rmi.NoSuchObjectException;
import java.util.concurrent.CompletableFuture;

public interface Cachable<Type, Ident, CacheClass extends Class> {
    Cache<Type, Ident> getCache();
    
    default CompletableFuture<Type> request(Ident ident) {
        return getCache().request(ident);
    }
    
    default Type get(Ident ident) {
        return getCache().get(ident);
    }
    
    default Type getOrCreate(Ident ident, Object... defaultConstructorParameter) {
        return getCache().getOrCreate(ident, defaultConstructorParameter);
    }
}
