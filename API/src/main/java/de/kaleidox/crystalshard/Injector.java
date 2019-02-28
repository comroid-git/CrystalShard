package de.kaleidox.crystalshard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

import de.kaleidox.crystalshard.api.util.Log;

import org.apache.logging.log4j.Logger;

public abstract class Injector {
    private static Logger logger;
    private static Collection<Injector> injectors;

    static {
        logger = Log.get(Injector.class);

        injectors = new ArrayList<>();
        for (Injector injector : ServiceLoader.load(Injector.class)) injectors.add(injector);
    }

    protected abstract Optional<Class> findOverride(Class forClass);

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> tClass, Object... args) {
        Class rClass = null;
        try {
            rClass = injectors.stream()
                    .map(injector -> injector.findOverride(tClass))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("No override found for class " + tClass));
        } catch (AssertionError e) {
            logger.fatal(e);
            System.exit(1);
        }

        assert tClass.isAssignableFrom(Objects.requireNonNull(rClass))
                : "Inassignability on returned class definition; contact the developer!";

        try {
            return (T) rClass.getConstructor(formTypeArray(args)).newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.fatal(e);
            System.exit(1);
        }
        return null;
    }

    private static Class<?>[] formTypeArray(Object[] args) {
        Class[] arr = new Class[args.length];
        for (int i = 0; i < args.length; i++) arr[i] = args[i].getClass();
        return arr;
    }
}