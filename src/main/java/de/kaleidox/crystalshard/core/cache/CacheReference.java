package de.kaleidox.crystalshard.core.cache;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class CacheReference<T> {
    private final long          keepAlive;
    private       T             reference;
    private       AtomicLong lastAccess;
    private       AtomicBoolean cached;
    
    CacheReference(T reference, long keepAlive) {
        this.reference = reference;
        this.keepAlive = keepAlive;
        this.lastAccess = new AtomicLong(System.currentTimeMillis());
        
        this.cached = new AtomicBoolean(reference != null);
    }
    
    public synchronized T getReference() {
        return reference;
    }
    
    public synchronized void setReference(T reference) {
        this.reference = reference;
        cached.set(true);
    }
    
    public synchronized boolean uncache() {
        if (canBeUncached()) {
            reference = null;
            cached.set(false);
            return true;
        }
        return false;
    }
    
    public boolean canBeUncached() {
        return (lastAccess.get() + keepAlive) < System.currentTimeMillis();
    }
    
    public synchronized void accessed() {
        lastAccess.set(System.currentTimeMillis());
    }
}
