package de.kaleidox.crystalshard.tests;

import de.kaleidox.crystalshard.DelegateBase;
import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.util.UtilDelegate;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import org.junit.Test;
import org.reflections.Reflections;

public class DelegatesTest {
    Logger logger;

    @Test(timeout = 10000)
    public void checkDelegateImplementations() {
        logger = new Logger(DelegatesTest.class);
        runTestOn(new Reflections(InternalDelegate.class.getPackageName()), InternalDelegate.class);
        runTestOn(new Reflections(CoreDelegate.class.getPackageName()), CoreDelegate.class);
        runTestOn(new Reflections(UtilDelegate.class.getPackageName()), UtilDelegate.class);
    }

    private <T extends DelegateBase> void runTestOn(Reflections reflections, Class<T> delegateClass) {
        logger.info("Running implementation tests for delegate: " + delegateClass.getSimpleName());
        int i = 0;
        for (Class<? extends T> delegateImplementation : reflections.getSubTypesOf(delegateClass)) {
            logger.info("Checking for implementations for delegate implementation " +
                    delegateImplementation.getSimpleName());
            i++;
            try {
                final DelegateBase delegate = delegateImplementation.getConstructor().newInstance();
                Hashtable<Class, Class> implementations = delegate.implementations();
                for (Class must : delegate.mustOverride()) {
                    if (!implementations.containsKey(must))
                        throw new RuntimeException(delegateImplementation.getSimpleName() +
                                " does not define an implementation for " + must.getName());
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new AssertionError(e);
            }
        }
        if (i == 0)
            throw new IllegalStateException("No delegate implementation found for " + delegateClass.getName() + "\n" +
                    "\tPlease provide an implementation within package: " + delegateClass.getPackageName());
    }
}
