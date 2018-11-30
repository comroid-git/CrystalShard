package de.kaleidox.crystalshard.util;

import de.kaleidox.crystalshard.InjectorBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

public abstract class UtilInjector extends InjectorBase {
    public final static UtilInjector injector;
    private final static Set<Class> mustOverride;

    static {
        UtilInjector using;
        ServiceLoader<UtilInjector> load = ServiceLoader.load(UtilInjector.class);
        Iterator<UtilInjector> iterator = load.iterator();
        if (iterator.hasNext()) using = iterator.next();
        else using = null;
        if (iterator.hasNext()) { // if no util injector available, this if wont run
            List<UtilInjector> allImplementations = new ArrayList<>();
            allImplementations.add(using);
            iterator.forEachRemaining(allImplementations::add);
            allImplementations.sort(Comparator.comparingInt(injector -> injector.getJdkVersion() * -1));
            using = allImplementations.get(0);
            logger.warn("More than one implementation for " + UtilInjector.class.getSimpleName() +
                    " found! Using " + using.getClass().getName());
        }
        injector = using;
        mustOverride = new HashSet<>();
        mustOverride.addAll(Arrays.asList(
                DiscordUtils.class,
                DefaultEmbed.class
        ));
    }

    public UtilInjector(Hashtable<Class, Class> implementations) {
        super(implementations, mustOverride);
    }

    public static <T> T newInstance(Class<T> tClass, Object... args) {
        if (injector == null) {
            logger.warn("No Util injector found. Please make sure you have any utilities implementation present. " +
                    "[NULL WAS RETURNED]");
            return null;
        }
        return injector.makeInstance(tClass, args);
    }
}
