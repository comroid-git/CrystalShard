package de.kaleidox.crystalshard.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.api.rest.DiscordRequest;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Contract;

public abstract class Adapter {
    private static FluentLogger log = FluentLogger.forEnclosingClass();
    private static Set<Adapter> implementations = new HashSet<>();

    protected ImplementationMapping mappingTool;

    public Adapter() {
        implementations.add(this);

        mappingTool = new ImplementationMapping();
    }

    @SuppressWarnings("unchecked")
    @Contract("null, _ -> null; _, _ -> _")
    public static <R> R create(Class<? super R> type, Object... args) {
        if (type == null) return null;

        String pkg = type.getPackage().getName();
        Class[] types = getTypes(args);

        return implementations.stream()
                .map(adapter -> adapter.mappingTool)
                .map(tool -> tool.find(type, types))
                .findFirst()
                .flatMap(it -> it)
                .map(inst -> inst.apply(args))
                .map(r -> (R) r)
                .orElseThrow(() -> new AssertionError("Class " + type.getName()
                        + " is not instantiable by CrystalShard"));
    }

    @SuppressWarnings("RedundantTypeArguments")
    @Contract("null -> fail")
    public static <R> DiscordRequest<R> request(Discord api) {
        if (api == null) throw new NullPointerException("API is null!");

        return Adapter.<DiscordRequest<R>>create(DiscordRequest.class, api);
    }

    public static <R, T> R staticOverride(Class<T> type, String method, Object... args) {
        // todo
        return null;
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

    private static Class[] getTypes(Object... args) {
        Class[] types = new Class[args.length];

        for (int i = 0; i < args.length; i++)
            types[i] = args[i].getClass();

        return types;
    }
}
