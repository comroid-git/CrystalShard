package de.comroid.crystalshard.adapter;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;

import de.comroid.crystalshard.CrystalShard;

import com.google.common.flogger.FluentLogger;

final class ImplementationMapping {
    private final static FluentLogger log = FluentLogger.forEnclosingClass();

    private final Map<ClassSignature, Instantiator> map = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked") <T> Optional<Instantiator<T>> find(Class<T> forClass, Class... signature) {
        ClassSignature<T> sig = new ClassSignature<>(forClass, signature);

        return map.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(sig))
                .peek(entry -> log.at(Level.FINEST).log("[%s] -> Matching implementation: %s", sig, entry.getKey()))
                .map(Map.Entry::getValue)
                .max(Comparator.comparingInt(inst -> inst.executable.getDeclaringClass()
                        .getInterfaces()
                        .length))
                .map(inst -> (Instantiator<T>) inst)
                // if no instantiator was found, try to find a methodBased one with zero arguments forClass
                .or(() -> map.entrySet()
                        .stream()
                        .filter(entry -> forClass.isAssignableFrom(entry.getKey().klass))
                        .filter(entry -> entry.getKey().methodBased)
                        .peek(entry -> log.at(Level.FINEST).log("[%s] -> Matching class override: %s",
                                sig, entry.getKey()))
                        .filter(entry -> entry.getKey().param.length == 0)
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .map(inst -> (Instantiator<T>) inst));
    }

    <T> ImplementationMapping implement(Class<T> type, Executable executable) {
        ClassSignature<T> sign = new ClassSignature<>(type, executable);

        if (map.containsKey(sign))
            throw new IllegalStateException(String.format("Illegal implementation declaration: implement %s with " +
                            "%s(%s) -> Duplicate Implementation Signature!\n\t\tPlease open an issue at %s",
                    type.getSimpleName(), sign.klass.getSimpleName(),
                    Arrays.toString(sign.param), CrystalShard.ISSUES_URL));

        map.put(sign, new Instantiator(executable));

        return this;
    }
    
    <T> ImplementationMapping implement(Class<T> type, final Class[] types, Function<Object[], T> construction) {
        ClassSignature<T> sign = new ClassSignature<>(type, types);

        if (map.containsKey(sign))
            throw new IllegalStateException(String.format("Illegal implementation declaration: implement %s with " +
                            "%s(%s) -> Duplicate Implementation Signature!\n\t\tPlease open an issue at %s",
                    type.getSimpleName(), sign.klass.getSimpleName(),
                    Arrays.toString(sign.param), CrystalShard.ISSUES_URL));

        map.put(sign, new Instantiator<>(construction));

        return this;
    }
}
