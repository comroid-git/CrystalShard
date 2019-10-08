package de.kaleidox.crystalshard.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
class Instantiator<T> implements Function<Object[], T> {
    private final Function<Object[], T> function;
    final @Nullable Executable executable;

    Instantiator(Executable executable) {
        this.executable = executable;
        // run checks on executable
        if (!executable.canAccess(this))
            throw new IllegalStateException("Implementation executable " + executable.getName()
                    + " cannot be accessed by " + this.getClass().getName() + " // " + toString());

        if (executable instanceof Method) {
            if (!Modifier.isStatic(executable.getModifiers()))
                throw new IllegalStateException("Implementation method " + executable.getName()
                        + " cannot be static");

            function = args -> {
                try {
                    return (T) ((Method) executable).invoke(null, args);
                } catch (IllegalAccessException ignored) {
                    // can never happen due to accessibility check in registration
                    throw new AssertionError();
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Implementation method threw an exception", e);
                }
            };
        } else if (executable instanceof Constructor) {
            if (Modifier.isAbstract(executable.getDeclaringClass().getModifiers()))
                throw new IllegalStateException("Implementation constructor " + executable.getName()
                        + " cannot be declared by an abstract class");

            function = args -> {
                try {
                    return ((Constructor<T>) executable).newInstance(args);
                } catch (IllegalAccessException | InstantiationException ignored) {
                    // can never happen due to accessibility and abstraction check in registration
                    throw new AssertionError();
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Implementation constructor threw an exception", e);
                }
            };
        } else throw new IllegalArgumentException("Unexpected subclass of java.lang.reflect.Executable: "
                + executable.getClass().getName());
    }

    public Instantiator(Function<Object[], T> construction) {
        this.executable = null;
        this.function = construction;
    }

    @Override
    public T apply(Object[] objects) {
        return function.apply(objects);
    }
}
