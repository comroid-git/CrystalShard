package de.kaleidox.crystalshard;

import de.kaleidox.crystalshard.logging.Logger;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Hashtable;
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
        try {
            tConstructor = extractConstructor((Constructor<T>[]) implementations.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey()
                            .getName()
                            .equalsIgnoreCase(tClass.getName()))
                    .map(Map.Entry::getValue)
                    .limit(1)
                    .peek(cls -> triedClass[0] = cls)
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("No override found for class: " + tClass.getName()))
                    .getConstructors(), args);
            return tConstructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("A construction exception occured: " + e.getClass().getSimpleName(), e);
        } catch (InvocationTargetException e) {
            assert tConstructor != null;
            throw new RuntimeException("An exception occured in constructor: "+tConstructor.toGenericString(), e);
        } catch (NoSuchMethodException e) {
            StringBuilder sb = new StringBuilder();
            if (triedClass[0] != null) for (Constructor constr : triedClass[0].getConstructors())
                sb.append("\t\t")
                        .append(constr.toGenericString())
                        .append("\n");
            throw new IllegalStateException("No constructor found for " + (triedClass[0] == null ? "" : ("class " +
                    triedClass[0].getSimpleName() + " with")) + " argument types:\n" + classTypes(args) + "\n" +
                    "\tAvailable constructors:\n" + sb.toString());
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
