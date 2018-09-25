package de.kaleidox.crystalshard.core.cache;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class represents a reference within the cache.
 *
 * @param <T> The type variable for the reference.
 * @param <R> The requestor type variable.
 */
class CacheReference<T extends CacheStorable, R> {
    private final long          keepAlive;
    private       String        refString;
    private       R             recentRequestor;
    private       Object[]      recentParameters;
    private       T             reference;
    private       AtomicLong    lastAccess;
    private       AtomicBoolean cached;
    
    CacheReference(T reference, long keepAlive, R recentRequestor, Object... recentParameters) {
        this.reference = reference;
        this.keepAlive = keepAlive;
        this.recentRequestor = recentRequestor;
        this.recentParameters = recentParameters;
        this.lastAccess = new AtomicLong(System.currentTimeMillis());
        
        if (Objects.nonNull(reference)) this.refString = reference.toString();
        
        this.cached = new AtomicBoolean(reference != null);
    }
    
// Override Methods
    @Override
    public String toString() {
        return "CacheReference{" + (refString == null ? "undefined reference" : refString) + "}";
    }
    
    public synchronized Object[] getRecentParameters() {
        return recentParameters;
    }
    
    public synchronized void setRecentParameters(Object[] recentParameters) {
        this.recentParameters = recentParameters;
    }
    
    public R getRecentRequestor() {
        return recentRequestor;
    }
    
    public void setRecentRequestor(R recentRequestor) {
        this.recentRequestor = recentRequestor;
    }
    
    public synchronized boolean isCached() {
        return cached.get();
    }
    
    public synchronized T getReference() {
        accessed();
        return reference;
    }
    
    public synchronized void setReference(T reference) {
        this.reference = reference;
        cached.set(true);
        if (Objects.nonNull(reference)) this.refString = reference.toString();
    }
    
    public synchronized void uncache() {
        reference = null;
        cached.set(false);
    }
    
    public boolean canBeUncached() {
        return (lastAccess.get() + keepAlive) < System.currentTimeMillis();
    }
    
    public synchronized void accessed() {
        lastAccess.set(System.currentTimeMillis());
    }
}
