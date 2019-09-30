package de.kaleidox.crystalshard.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.api.rest.DiscordRequest;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Contract;

public abstract class Adapter {
    private static FluentLogger log = FluentLogger.forEnclosingClass();
    
    protected ImplementationMapping mappingTool = new ImplementationMapping();

    @Contract("null, _ -> null; _, _ -> _")
    public static <R> R create(Class<? super R> type, Object... args) {
        if (type == null) return null;

        String pkg = type.getPackage().getName();
        Class[] types = getTypes(args);

        if (pkg.startsWith("de.kaleidox.crystalshard.core."))
            return instantiate(CoreAdapter.adapter.mappingTool.find(type, types), args);
        else if (pkg.startsWith("de.kaleidox.crystalshard.api.")
                || pkg.startsWith("de.kaleidox.crystalshard.impl."))
            return instantiate(ImplAdapter.adapter.mappingTool.find(type, types), args);
        else if (pkg.startsWith("de.kaleidox.crystalshard.util."))
            return instantiate(UtilAdapter.adapter.mappingTool.find(type, types), args);

        throw new AssertionError("Class " + type.getSimpleName() + " is not instantiable by CrystalShard!");
    }

    @SuppressWarnings("RedundantTypeArguments")
    @Contract("null -> fail")
    public static <R> DiscordRequest<R> request(Discord api) {
        if (api == null) throw new NullPointerException("API is null!");

        return Adapter.<DiscordRequest<R>>create(DiscordRequest.class, api);
    }

    @SuppressWarnings("unchecked")
    public static <R, T> R staticOverride(Class<T> targetClass, String method, Object... args) {
        if (targetClass == null) return null;

        String pkg = targetClass.getPackage().getName();
        Constructor<? extends T> override;

        if (pkg.startsWith("de.kaleidox.crystalshard.core."))
            override = CoreAdapter.adapter.mappingTool.find(targetClass);
        else if (pkg.startsWith("de.kaleidox.crystalshard.api.")
                || pkg.startsWith("de.kaleidox.crystalshard.impl."))
            override = ImplAdapter.adapter.mappingTool.find(targetClass);
        else if (pkg.startsWith("de.kaleidox.crystalshard.util."))
            override = UtilAdapter.adapter.mappingTool.find(targetClass);
        else throw new AssertionError("Class " + targetClass.getSimpleName()
                    + " is not instantiable by CrystalShard!");

        Method declaredMethod = null;

        try {
            declaredMethod = override.getDeclaringClass()
                    .getDeclaredMethod(method, getTypes(args));

            return (R) declaredMethod.invoke(null, args);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Class " + override + " does not implement static method " + method, e);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Cannot access method: " + declaredMethod, e);
        } catch (InvocationTargetException e) {
            throw new AssertionError("Method threw an exception!", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T, R extends T> R instantiate(Constructor<T> constructor, Object... args) {
        Class[] types = getTypes(args);

        try {
            R inst = (R) constructor.newInstance(args);
            log.at(Level.FINEST).log("New instance created: %s", inst);
            return inst;
        } catch (InstantiationException e) {
            throw new AssertionError("Constructor " + constructor.getName() + " is abstract!", e);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Cannot access constructor: " + constructor, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Constructor threw an exception!", e);
        }
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
