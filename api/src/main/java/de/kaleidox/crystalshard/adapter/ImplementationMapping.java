package de.kaleidox.crystalshard.adapter;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import de.kaleidox.crystalshard.CrystalShard;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Nullable;
 
final class ImplementationMapping {
    private final static FluentLogger log = FluentLogger.forEnclosingClass();
    
    private final Map<ClassSignature, Constructor> map = new ConcurrentHashMap<>();

    <T> @Nullable Constructor<? extends T> find(Class<T> forClass, Class... signature) {
        ClassSignature<T> sig = new ClassSignature<>(forClass, signature);

        //noinspection unchecked
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(sig))
                .peek(entry -> log.at(Level.FINEST).log("[%s] -> Matching implementation: %s", sig, entry.getKey()))
                .map(Map.Entry::getValue)
                .max(Comparator.comparingInt(constr -> constr.getDeclaringClass()
                        .getInterfaces()
                        .length))
                .map(constr -> (Constructor<? extends T>) constr)
                .orElseThrow(() -> new NoSuchElementException("Could not find implementation for signature: " + sig));
    }

    <T> ImplementationMapping implement(Class<T> type, Class<? extends T> with, Class... sig) {
        try {
            ClassSignature<T> signature = new ClassSignature<>(type, sig);
            Constructor<? extends T> constr = with.getConstructor(sig);

            if (map.containsKey(signature))
                throw new IllegalStateException(String.format("Illegal implementation declaration: implement %s with " +
                                "%s(%s) -> Duplicate Implementation Signature!\n\t\tPlease open an issue at %s",
                        type.getSimpleName(), with.getSimpleName(), Arrays.toString(sig), CrystalShard.ISSUES_URL));

            map.put(signature, constr);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(String.format("Illegal implementation declaration: implement %s with " +
                            "%s(%s) -> Unknown Implementation Constructor!\n\t\tPlease open an issue at %s",
                    type.getSimpleName(), with.getSimpleName(), Arrays.toString(sig), CrystalShard.ISSUES_URL), e);
        }

        return this;
    }

    private static class ClassSignature<T> {
        private final Class<T> klass;
        private final Class[] sign;

        ClassSignature(Class<T> klass, Class[] sign) {
            this.klass = klass;
            this.sign = sign;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ClassSignature)
                return klass.isAssignableFrom(((ClassSignature) obj).klass)
                        && allAssignable(sign, ((ClassSignature) obj).sign);

            return false;
        }

        @Override
        public String toString() {
            return String.format("ClassSignature %s(%s)", klass.getName(), Arrays.toString(sign));
        }

        private static boolean allAssignable(Class[] signBase, Class[] signTarget) {
            if (signBase.length != signTarget.length) return false;

            for (int i = 0; i < signBase.length; i++) {
                Class bc = signBase[i];
                Class tc = signTarget[i];

                //noinspection unchecked
                if (!bc.isAssignableFrom(tc))
                    return false;
            }

            return true;
        }
    }
}
