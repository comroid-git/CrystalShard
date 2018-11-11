package de.kaleidox.crystalshard.tests;

import de.kaleidox.crystalshard.InjectorBase;
import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.util.UtilInjector;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import org.junit.Test;
import org.reflections.Reflections;

public class DelegatesTest {
    Logger logger;

    @Test(timeout = 10000)
    public void checkDelegateImplementations() {
        logger = new Logger(DelegatesTest.class);
        runTestOn(new Reflections(InternalInjector.class.getPackage().getName()), InternalInjector.class);
        runTestOn(new Reflections(CoreInjector.class.getPackage().getName()), CoreInjector.class);
        runTestOn(new Reflections(UtilInjector.class.getPackage().getName()), UtilInjector.class);
    }

    private <T extends InjectorBase> void runTestOn(Reflections reflections, Class<T> delegateClass) {
        logger.info("Running implementation tests for delegate: " + delegateClass.getSimpleName());
        int i = 0;
        for (Class<? extends T> delegateImplementation : reflections.getSubTypesOf(delegateClass)) {
            logger.info("Checking for implementations for delegate implementation " +
                    delegateImplementation.getSimpleName());
            i++;
            try {
                final InjectorBase delegate = delegateImplementation.getConstructor().newInstance();
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
                    "\tPlease provide an implementation within package: " + delegateClass.getPackage().getName());
    }
}
