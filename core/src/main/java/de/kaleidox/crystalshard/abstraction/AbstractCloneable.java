package de.kaleidox.crystalshard.abstraction;

import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.Contract;

public abstract class AbstractCloneable<T> implements Cloneable {
    protected UUID uuid;
    
    protected AbstractCloneable() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractCloneable)
            return ((AbstractCloneable) obj).uuid.equals(uuid);
        
        return false;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override 
    public abstract T clone();
    
    @Contract(value = "null -> fail; _ -> this", mutates = "this")
    protected T equalize(T to) throws IllegalArgumentException {
        if (!(to instanceof AbstractCloneable))
            throw new IllegalArgumentException("Cannot equalize to non-AbstractCloneable type");
        
        this.uuid = ((AbstractCloneable) Objects.requireNonNull(to, "Cannot equalize to null object")).uuid;

        //noinspection unchecked
        return (T) this;
    }
}
