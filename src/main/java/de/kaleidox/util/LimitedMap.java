package de.kaleidox.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;

public class LimitedMap<K, V> extends ConcurrentHashMap<K, V> {
    private final Predicate<Entry<K, V>> limiter;

    public LimitedMap(Predicate<Entry<K, V>> limiter) {
        super();
        this.limiter = limiter;
    }

    public LimitedMap(Map<K, V> map, Predicate<Entry<K, V>> limiter) {
        super(map);
        this.limiter = limiter;
    }

    @Override
    public int size() {
        refreshLimiter();
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        refreshLimiter();
        return super.isEmpty();
    }

    @Override
    public V get(Object key) {
        refreshLimiter();
        return super.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        refreshLimiter();
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        refreshLimiter();
        return super.containsValue(value);
    }

    @Override
    public V put(K key, V value) {
        refreshLimiter();
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        refreshLimiter();
        super.putAll(m);
    }

    @Override
    public V remove(Object key) {
        refreshLimiter();
        return super.remove(key);
    }

    @Override
    public void clear() {
        refreshLimiter();
        super.clear();
    }

    @Override
    public KeySetView<K, V> keySet() {
        refreshLimiter();
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        refreshLimiter();
        return super.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        refreshLimiter();
        return super.entrySet();
    }

    @Override
    public int hashCode() {
        refreshLimiter();
        return super.hashCode();
    }

    @Override
    public String toString() {
        refreshLimiter();
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        refreshLimiter();
        return super.equals(o);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        refreshLimiter();
        return super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        refreshLimiter();
        return super.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        refreshLimiter();
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        refreshLimiter();
        return super.replace(key, value);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        refreshLimiter();
        return super.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        refreshLimiter();
        super.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        refreshLimiter();
        super.replaceAll(function);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        refreshLimiter();
        return super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        refreshLimiter();
        return super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        refreshLimiter();
        return super.compute(key, remappingFunction);
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        refreshLimiter();
        return super.merge(key, value, remappingFunction);
    }

    @Override
    public boolean contains(Object value) {
        refreshLimiter();
        return super.contains(value);
    }

    @Override
    public Enumeration<K> keys() {
        refreshLimiter();
        return super.keys();
    }

    @Override
    public Enumeration<V> elements() {
        refreshLimiter();
        return super.elements();
    }

    @Override
    public long mappingCount() {
        refreshLimiter();
        return super.mappingCount();
    }

    @Override
    public KeySetView<K, V> keySet(V mappedValue) {
        refreshLimiter();
        return super.keySet(mappedValue);
    }

    @Override
    public void forEach(long parallelismThreshold, BiConsumer<? super K, ? super V> action) {
        refreshLimiter();
        super.forEach(parallelismThreshold, action);
    }

    @Override
    public <U> void forEach(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> transformer, Consumer<? super U> action) {
        refreshLimiter();
        super.forEach(parallelismThreshold, transformer, action);
    }

    @Override
    public <U> U search(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> searchFunction) {
        refreshLimiter();
        return super.search(parallelismThreshold, searchFunction);
    }

    @Override
    public <U> U reduce(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        refreshLimiter();
        return super.reduce(parallelismThreshold, transformer, reducer);
    }

    @Override
    public double reduceToDouble(long parallelismThreshold, ToDoubleBiFunction<? super K, ? super V> transformer, double basis, DoubleBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceToDouble(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public long reduceToLong(long parallelismThreshold, ToLongBiFunction<? super K, ? super V> transformer, long basis, LongBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceToLong(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public int reduceToInt(long parallelismThreshold, ToIntBiFunction<? super K, ? super V> transformer, int basis, IntBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceToInt(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public void forEachKey(long parallelismThreshold, Consumer<? super K> action) {
        refreshLimiter();
        super.forEachKey(parallelismThreshold, action);
    }

    @Override
    public <U> void forEachKey(long parallelismThreshold, Function<? super K, ? extends U> transformer, Consumer<? super U> action) {
        refreshLimiter();
        super.forEachKey(parallelismThreshold, transformer, action);
    }

    @Override
    public <U> U searchKeys(long parallelismThreshold, Function<? super K, ? extends U> searchFunction) {
        refreshLimiter();
        return super.searchKeys(parallelismThreshold, searchFunction);
    }

    @Override
    public K reduceKeys(long parallelismThreshold, BiFunction<? super K, ? super K, ? extends K> reducer) {
        refreshLimiter();
        return super.reduceKeys(parallelismThreshold, reducer);
    }

    @Override
    public <U> U reduceKeys(long parallelismThreshold, Function<? super K, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        refreshLimiter();
        return super.reduceKeys(parallelismThreshold, transformer, reducer);
    }

    @Override
    public double reduceKeysToDouble(long parallelismThreshold, ToDoubleFunction<? super K> transformer, double basis, DoubleBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceKeysToDouble(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public long reduceKeysToLong(long parallelismThreshold, ToLongFunction<? super K> transformer, long basis, LongBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceKeysToLong(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public int reduceKeysToInt(long parallelismThreshold, ToIntFunction<? super K> transformer, int basis, IntBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceKeysToInt(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public void forEachValue(long parallelismThreshold, Consumer<? super V> action) {
        refreshLimiter();
        super.forEachValue(parallelismThreshold, action);
    }

    @Override
    public <U> void forEachValue(long parallelismThreshold, Function<? super V, ? extends U> transformer, Consumer<? super U> action) {
        refreshLimiter();
        super.forEachValue(parallelismThreshold, transformer, action);
    }

    @Override
    public <U> U searchValues(long parallelismThreshold, Function<? super V, ? extends U> searchFunction) {
        refreshLimiter();
        return super.searchValues(parallelismThreshold, searchFunction);
    }

    @Override
    public V reduceValues(long parallelismThreshold, BiFunction<? super V, ? super V, ? extends V> reducer) {
        refreshLimiter();
        return super.reduceValues(parallelismThreshold, reducer);
    }

    @Override
    public <U> U reduceValues(long parallelismThreshold, Function<? super V, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        refreshLimiter();
        return super.reduceValues(parallelismThreshold, transformer, reducer);
    }

    @Override
    public double reduceValuesToDouble(long parallelismThreshold, ToDoubleFunction<? super V> transformer, double basis, DoubleBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceValuesToDouble(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public long reduceValuesToLong(long parallelismThreshold, ToLongFunction<? super V> transformer, long basis, LongBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceValuesToLong(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public int reduceValuesToInt(long parallelismThreshold, ToIntFunction<? super V> transformer, int basis, IntBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceValuesToInt(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public void forEachEntry(long parallelismThreshold, Consumer<? super Entry<K, V>> action) {
        refreshLimiter();
        super.forEachEntry(parallelismThreshold, action);
    }

    @Override
    public <U> void forEachEntry(long parallelismThreshold, Function<Entry<K, V>, ? extends U> transformer, Consumer<? super U> action) {
        refreshLimiter();
        super.forEachEntry(parallelismThreshold, transformer, action);
    }

    @Override
    public <U> U searchEntries(long parallelismThreshold, Function<Entry<K, V>, ? extends U> searchFunction) {
        refreshLimiter();
        return super.searchEntries(parallelismThreshold, searchFunction);
    }

    @Override
    public Entry<K, V> reduceEntries(long parallelismThreshold, BiFunction<Entry<K, V>, Entry<K, V>, ? extends Entry<K, V>> reducer) {
        refreshLimiter();
        return super.reduceEntries(parallelismThreshold, reducer);
    }

    @Override
    public <U> U reduceEntries(long parallelismThreshold, Function<Entry<K, V>, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        refreshLimiter();
        return super.reduceEntries(parallelismThreshold, transformer, reducer);
    }

    @Override
    public double reduceEntriesToDouble(long parallelismThreshold, ToDoubleFunction<Entry<K, V>> transformer, double basis, DoubleBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceEntriesToDouble(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public long reduceEntriesToLong(long parallelismThreshold, ToLongFunction<Entry<K, V>> transformer, long basis, LongBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceEntriesToLong(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    public int reduceEntriesToInt(long parallelismThreshold, ToIntFunction<Entry<K, V>> transformer, int basis, IntBinaryOperator reducer) {
        refreshLimiter();
        return super.reduceEntriesToInt(parallelismThreshold, transformer, basis, reducer);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        refreshLimiter();
        return super.clone();
    }

    @SuppressWarnings("StreamToLoop")
    private void refreshLimiter() {
        entrySet().forEach(entry -> {
            if (!limiter.test(entry)) {
                super.remove(entry.getKey());
            }
        });
    }
}
