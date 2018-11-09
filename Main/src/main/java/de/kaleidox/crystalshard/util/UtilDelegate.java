package de.kaleidox.crystalshard.util;

import de.kaleidox.crystalshard.DelegateBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

public abstract class UtilDelegate extends DelegateBase {
    public final static UtilDelegate delegate;
    private final static Set<Class> mustOverride;

    static {
        UtilDelegate using;
        ServiceLoader<UtilDelegate> load = ServiceLoader.load(UtilDelegate.class);
        Iterator<UtilDelegate> iterator = load.iterator();
        if (iterator.hasNext()) using = iterator.next();
        else using = null;
        if (iterator.hasNext()) { // if no util delegate available, this if wont run
            List<UtilDelegate> allImplementations = new ArrayList<>();
            allImplementations.add(using);
            iterator.forEachRemaining(allImplementations::add);
            allImplementations.sort(Comparator.comparingInt(delegate -> delegate.getJdkVersion() * -1));
            using = allImplementations.get(0);
            logger.warn("More than one implementation for " + UtilDelegate.class.getSimpleName() +
                    " found! Using " + using.getClass().getName());
        }
        delegate = using;
        mustOverride = new HashSet<>();
        mustOverride.addAll(Arrays.asList(
                DiscordUtils.class,
                DefaultEmbed.class
        ));
    }

    public UtilDelegate(Hashtable<Class, Class> implementations) {
        super(implementations, mustOverride);
    }

    public static <T> T newInstance(Class<T> tClass, Object... args) {
        if (delegate == null)
            throw new IllegalStateException("No Util delegate found. Please " +
                    "make sure you have any utilities implementation present.");
        return delegate.makeInstance(tClass, args);
    }
}
