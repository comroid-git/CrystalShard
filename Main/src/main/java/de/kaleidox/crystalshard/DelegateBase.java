package de.kaleidox.crystalshard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Hashtable;

public class DelegateBase {
    private final Hashtable<Class, Class> implementations;

    public DelegateBase(Hashtable<Class, Class> implementations) {
        this.implementations = implementations;
    }

    @SuppressWarnings("unchecked")
    protected <T> T makeInstance(Class<T> tClass, Object... args) {
        final Class[] triedClass = {null};
        try {
            Constructor<T> tConstructor = extractConstructor(((Constructor<T>[]) implementations.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey()
                            .getName()
                            .equalsIgnoreCase(tClass.getName()))
                    .findAny()
                    .map(entry -> {
                        triedClass[0] = entry.getValue();
                        return entry;
                    })
                    .orElseThrow(() -> new IllegalStateException("No override found for class: " + tClass.getName()))
                    .getValue()
                    .getConstructors()), args);
            return tConstructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("A construction exception occured:", e);
        } catch (NoSuchMethodException e) {
            StringBuilder sb = new StringBuilder();
            if (triedClass[0] != null) for (Constructor constr : triedClass[0].getConstructors())
                sb.append("\t\t")
                        .append(constr.toGenericString())
                        .append("\n");
            throw new IllegalStateException("No constructor found for " + (triedClass[0] == null ? "" : ("class " +
                    triedClass[0].getSimpleName() + " with")) + " argument types:\n" + classTypes(args) + "\n" +
                    "\tAvailable constructors:\n"+sb.toString());
        }
    }

    private String classTypes(Object[] args) {
        Class[] cls = new Class[args.length];
        for (int i = 0; i < args.length; i++) cls[i] = args[i].getClass();
        return Arrays.toString(cls);
    }

    private <T> Constructor<T> extractConstructor(Constructor<T>[] constrs, Object[] args) throws NoSuchMethodException {
        int fits;
        for (Constructor constr : constrs) {
            fits = 0;
            Class[] conTypes = constr.getParameterTypes();
            for (int i = 0; i < args.length; i++) {
                //noinspection unchecked
                if (conTypes[i].isAssignableFrom(args[i].getClass())) fits++;
            }
            if (fits == args.length) //noinspection unchecked
                return constr;
        }
        throw new NoSuchMethodException("No Constructor found matching the given parameters!");
    }
}
