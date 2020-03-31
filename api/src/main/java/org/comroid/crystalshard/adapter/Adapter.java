package org.comroid.crystalshard.adapter;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.logging.Level;

import org.jetbrains.annotations.Contract;

public abstract class Adapter {
    public static Optional<Class<?>> getApiClass(final Object from) {
        if (from instanceof Class)
            return Optional.ofNullable(getApiClass_r((Class<?>) from));

        return Optional.ofNullable(getApiClass_r(from.getClass()));
    }

    private static <T> Class<?> getApiClass_r(Class<?> impl) {
        final Class<?>[] interfaces = impl.getInterfaces();

        for (Class<?> anInterface : interfaces) {
            if (anInterface.isAnnotationPresent(MainAPI.class))
                return anInterface;
            else {
                final Class<?> yield = getApiClass_r(anInterface);
                
                if (yield != null) return yield;
            }
        }
        
        return null;
    }

    @Contract("null, _ -> fail; _, _ -> _")
    public static <R> R require(final Class<? super R> type, final Object... param) {
    }

    protected static <T extends Adapter> T loadAdapter(Class<T> adapter) {
        ServiceLoader<T> loader = ServiceLoader.load(adapter);
        Iterator<T> iterator = loader.iterator();

        if (!iterator.hasNext())
            throw new IllegalStateException("No implementation of " + adapter.getSimpleName() + " was found! " +
                    "Are you missing a module?");

        T impl = iterator.next();

        if (iterator.hasNext())
            log.at(Level.FINE).log("Multiple implementations of %s were found!", adapter.getSimpleName());

        return impl;
    }
}
