package de.kaleidox.crystalshard.util.helpers;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class SetHelper extends CollectionHelper {
    public static <T, C extends Set> Set<T> of(Class<C> typeClass, T... items) {
        try {
            Set<T> set = typeClass.getConstructor()
                    .newInstance();
            set.addAll(Arrays.asList(items));
            return set;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}
