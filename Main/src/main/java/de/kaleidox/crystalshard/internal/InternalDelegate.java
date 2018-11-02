package de.kaleidox.crystalshard.internal;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.DelegateBase;
import de.kaleidox.crystalshard.main.Discord;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.ServiceLoader;

public abstract class InternalDelegate extends DelegateBase {
    public final static InternalDelegate delegate;

    static {
        ServiceLoader<InternalDelegate> load = ServiceLoader.load(InternalDelegate.class);
        Iterator<InternalDelegate> iterator = load.iterator();
        if (iterator.hasNext()) delegate = iterator.next();
        else throw new IllegalStateException("No implementation for " + InternalDelegate.class.getName() + " found!");
        if (iterator.hasNext())
            throw new IllegalStateException("More than one implementation for " + InternalDelegate.class.getName() + " found!");
    }

    public InternalDelegate(Hashtable<Class, Class> implementations) {
        super(implementations);
    }

    public static <T> T newInstance(Class<T> tClass, Object... args) {
        return delegate.makeInstance(tClass, args);
    }

    public static void tryHandle(Discord discord, JsonNode data) {
        delegate.tryHandleDelegate(discord, data);
    }

    protected abstract void tryHandleDelegate(Discord discord, JsonNode data);
}
