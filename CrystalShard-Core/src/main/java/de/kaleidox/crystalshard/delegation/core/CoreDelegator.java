package de.kaleidox.crystalshard.delegation.core;

import de.kaleidox.crystalshard.core.delegation.ThreadPool;

import java.lang.reflect.InvocationTargetException;

public class CoreDelegator {
    private static final String delegateClassName = "de.kaleidox.crystalshard.delegation.core.CoreDelegateFactoryImpl";
    private static final CoreDelegateFactory delegateFactory;
    
    static {
        // Init Delegate Factory
        try {
            //noinspection unchecked
            Class<CoreDelegateFactory> delegateFactoryClass =
                    (Class<CoreDelegateFactory>) Class.forName(delegateClassName);
            delegateFactory = delegateFactoryClass.getConstructor().newInstance();
        } catch (ClassNotFoundException ignored) {
            throw new IllegalStateException("No implementation found for ["+delegateClassName+"]!");
        } catch (ClassCastException ignored) {
            throw new IllegalStateException("CoreDelegateFactoryImpl is of wrong type!");
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("Could not instantiate CoreDelegateFactory!");
        }
    }
    
    public static ThreadPool getThreadPool();
}
