package de.kaleidox.util.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CustomCollectors {
    // Static Fields
    public static final Set<Collector.Characteristics> CH_ID              = Collections.unmodifiableSet(EnumSet.of(
            Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_NOID            = Collections.emptySet();
    static final        Set<Collector.Characteristics> CH_CONCURRENT_ID   = Collections.unmodifiableSet(EnumSet.of(
            Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    static final        Set<Collector.Characteristics> CH_CONCURRENT_NOID = Collections.unmodifiableSet(EnumSet.of(
            Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED));
    static final        Set<Collector.Characteristics> CH_UNORDERED_ID    = Collections.unmodifiableSet(EnumSet.of(
            Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    
    // Static members
    public static <T> Collector<Collection<T>, Collection<T>, ArrayList<T>> collectionMerge() {
        return new CustomCollectorImpl<>(ArrayList::new, Collection::addAll, (left, right) -> {
            left.addAll(right);
            return left;
        }, CH_ID);
    }
    
    public static <T, L extends Collection<T>> Collector<Collection<T>, L, L> collectionMerge(Supplier<L> collectionSupplier) {
        return new CustomCollectorImpl<>(collectionSupplier, Collection::addAll, (left, right) -> {
            left.addAll(right);
            return left;
        }, CH_ID);
    }
    
    /**
     * Merges a stream of Maps into one larger Map.
     * <B>This collector swallows any duplicate entries.</B>
     *
     * @param <K> The key type of the maps.
     * @param <V> The value type of the maps.
     * @return A collector to collect maps.
     */
    public static <K, V> Collector<Map<K, V>, HashMap<K, V>, HashMap<K, V>> mapMerge() {
        return new CustomCollectorImpl<>(HashMap::new, (l, r) -> r.forEach(l::putIfAbsent), (l, r) -> {
            r.forEach(l::putIfAbsent);
            return l;
        }, CH_ID);
    }
    
    public static class CustomCollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A>          supplier;
        private final BiConsumer<A, T>     accumulator;
        private final BinaryOperator<A>    combiner;
        private final Function<A, R>       finisher;
        private final Set<Characteristics> characteristics;
        
        public CustomCollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
                                   Function<A, R> finisher, Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }
        
        public CustomCollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
                                   Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }
        
        // Override Methods
        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }
        
        @Override
        public Supplier<A> supplier() {
            return supplier;
        }
        
        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }
        
        @Override
        public Function<A, R> finisher() {
            return finisher;
        }
        
        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
        
        // Static members
        private static <I, R> Function<I, R> castingIdentity() {
            return i -> (R) i;
        }
    }
}
