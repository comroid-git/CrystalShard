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

public class InjectorTest {
    Logger logger;

    @Test(timeout = 10000)
    public void checkinjectorImplementations() {
        logger = new Logger(InjectorTest.class);
        runTestOn(new Reflections(InternalInjector.class.getPackage().getName()), InternalInjector.class);
        runTestOn(new Reflections(CoreInjector.class.getPackage().getName()), CoreInjector.class);
        runTestOn(new Reflections(UtilInjector.class.getPackage().getName()), UtilInjector.class);
    }

    private <T extends InjectorBase> void runTestOn(Reflections reflections, Class<T> injectorClass) {
        logger.info("Running implementation tests for injector: " + injectorClass.getSimpleName());
        int i = 0;
        for (Class<? extends T> injectorImplementation : reflections.getSubTypesOf(injectorClass)) {
            logger.info("Checking for implementations for injector implementation " +
                    injectorImplementation.getSimpleName());
            i++;
            try {
                final InjectorBase injector = injectorImplementation.getConstructor().newInstance();
                Hashtable<Class, Class> implementations = injector.implementations();
                for (Class must : injector.mustOverride()) {
                    if (!implementations.containsKey(must))
                        throw new RuntimeException(injectorImplementation.getSimpleName() +
                                " does not define an implementation for " + must.getName());
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new AssertionError(e);
            }
        }
        if (i == 0)
            throw new IllegalStateException("No injector implementation found for " + injectorClass.getName() + "\n" +
                    "\tPlease provide an implementation within package: " + injectorClass.getPackage().getName());
    }
}
