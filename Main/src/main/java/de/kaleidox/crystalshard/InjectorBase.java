package de.kaleidox.crystalshard;

import de.kaleidox.crystalshard.logging.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class InjectorBase {
    protected final static Logger logger = new Logger(InjectorBase.class);
    private final Hashtable<Class, Class> implementations;
    private final Set<Class> mustOverride;

    public InjectorBase(Hashtable<Class, Class> implementations, Set<Class> mustOverride) {
        this.implementations = implementations;
        this.mustOverride = mustOverride;
    }

    /**
     * Used to determine what injector to use if there are several implementations.
     * Old semantics like {@code 1.8} will return just {@code 8}.
     *
     * @return The numeric version for the JDK that the injector was made for.
     */
    public abstract int getJdkVersion();

    public Hashtable<Class, Class> implementations() {
        return implementations;
    }

    public Set<Class> mustOverride() {
        return mustOverride;
    }

    @SuppressWarnings("unchecked")
    protected <T> T makeInstance(Class<T> tClass, Object... args) {
        final Class[] triedClass = {null};
        Constructor<T> tConstructor = null;
        Constructor<T>[] constrs = implementations.entrySet()
                .stream()
                .filter(entry -> entry.getKey()
                        .getName()
                        .equalsIgnoreCase(tClass.getName()))
                .map(Map.Entry::getValue)
                .limit(1)
                .peek(cls -> triedClass[0] = cls)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No override found for class: " + tClass.getName()))
                .getConstructors();
        try {
            tConstructor = extractConstructor(constrs, args);
        } catch (NoSuchMethodException e) {
            try {
                tConstructor = extractNullConstructor(e, constrs, args);
            } catch (NoSuchMethodException f) {
                StringBuilder sb = new StringBuilder();
                if (triedClass[0] != null) for (Constructor constr : triedClass[0].getConstructors())
                    sb.append("\t\t")
                            .append(constr.toGenericString())
                            .append("\n");
                throw new IllegalStateException("No constructor found for " + (triedClass[0] == null ? "" : ("class " +
                        triedClass[0].getSimpleName() + " with")) + " argument types:\n" + classTypes(args) + "\n" +
                        "\tAvailable constructors:\n" + sb.toString(), f);
            }
        }
        return inst(tConstructor, args);
    }

    private <T> T inst(Constructor<T> constr, Object[] args) {
        try {
            return constr.newInstance(args);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("A construction exception occured: " + e.getClass().getSimpleName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("An exception occured in constructor: " + constr.toGenericString(), e);
        }
    }

    private String classTypes(Object[] args) {
        String[] cls = new String[args.length];
        for (int i = 0; i < args.length; i++) cls[i] = (args[i] == null ? "null" : args[i].getClass().toString());
        return Arrays.toString(cls);
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> extractConstructor(Constructor<T>[] constrs, Object[] args) throws NoSuchMethodException {
        int fits;
        for (Constructor constr : constrs) {
            fits = 0;
            Class[] conTypes = constr.getParameterTypes();
            if (conTypes.length != args.length) continue;
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    fits++;
                    continue;
                }
                if (conTypes[i].isAssignableFrom(args[i].getClass())) fits++;
            }
            if (fits == args.length) return constr;
        }
        throw new NoSuchMethodException("No Constructor found matching the given parameters!");
    }

    private <T> Constructor<T> extractNullConstructor(NoSuchMethodException cause,
                                                      Constructor<T>[] constrs,
                                                      Object[] args) throws NoSuchMethodException {
        class Constr implements Comparable<Constr> {
            private final Constructor<T> constructor;
            private final int match;

            private Constr(Constructor<T> constructor, int match) {
                this.constructor = constructor;
                this.match = match;
            }

            @Override
            public int compareTo(Constr o) {
                return Integer.compare(match, o.match);
            }
        }

        List<Constr> patch = new ArrayList<>();
        for (Constructor<T> constr : constrs) {
            int match = 0;
            for (int i = 0; i < constr.getParameterTypes().length; i++) {
                Class<?> type = constr.getParameterTypes()[i];
                if (args[i] == null) {
                    match++;
                    continue;
                }
                if (type.isAssignableFrom(args[i].getClass())) match++;
            }
            if (args.length == constr.getParameterCount())
                patch.add(new Constr(constr, match));
        }
        if (patch.size() == 0) throw new RuntimeException("No null-suitable constructor found!", cause);
        Collections.sort(patch);
        return Collections.max(patch).constructor;
    }
}
