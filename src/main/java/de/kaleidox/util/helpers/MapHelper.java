package de.kaleidox.util.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

/**
 * This class contains several methods that are helpful when handling any kind of {@link Map}.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MapHelper {
    public static <K, V> V getEquals(Map<K, V> map, K key, V valueIfAbsent) {
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(key))
                .map(Map.Entry::getValue)
                .findAny()
                .orElse(valueIfAbsent);
    }

    /**
     * Another implementation of {@link Map#containsKey(Object)}, but uses {@link Object#equals(Object)}
     * instead of comparing hash codes.
     *
     * @param map The map to check in.
     * @param key The key to look for.
     * @param <K> Type variable for the keys.
     * @param <V> Type variable for the values.
     * @return Whether the map contains the key.
     * @see Map#containsKey(Object)
     */
    public static <K, V> boolean containsKey(Map<K, V> map, K key) {
        return map.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .anyMatch(check -> check.equals(key));
    }

    /**
     * Another implementation of {@link Map#containsValue(Object)}, but uses {@link Object#equals(Object)}
     * instead of comparing hash codes.
     *
     * @param map   The map to check in.
     * @param value The value to look for.
     * @param <K>   Type variable for the keys.
     * @param <V>   Type variable for the values.
     * @return Whether the map contains the key.
     * @see Map#containsValue(Object)
     */
    public static <K, V> boolean containsValue(Map<K, V> map, V value) {
        return map.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .anyMatch(check -> check.equals(value));
    }

    /**
     * Checks whether the given map contains any Entry whose key {@link Object#equals(Object)} the value
     * returned by the {@code extractor} for that key.
     *
     * @param map       The map to check in.
     * @param value     The value to look for.
     * @param extractor The Function to extract the value out of a key.
     * @param <K>       Type variable for the keys.
     * @param <V>       Type variable for the values.
     * @param <T>       Type variable for the item to check for.
     * @return Whether the map contains a key that can be mapped to the value.
     */
    public static <K, V, T> boolean containsKey(Map<K, V> map, T value, Function<K, T> extractor) {
        return map.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .map(extractor)
                .anyMatch(t -> t.equals(value));
    }

    /**
     * Checks whether the given map contains any Entry whose value {@link Object#equals(Object)} the value
     * returned by the {@code extractor} for that value.
     *
     * @param map       The map to check in.
     * @param value     The value to look for.
     * @param extractor The Function to extract the value out of a value.
     * @param <K>       Type variable for the keys.
     * @param <V>       Type variable for the values.
     * @param <T>       Type variable for the item to check for.
     * @return Whether the map contains a key that can be mapped to the value.
     */
    public static <K, V, T> boolean containsValue(Map<K, V> map, T value, Function<V, T> extractor) {
        return map.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map(extractor)
                .anyMatch(t -> t.equals(value));
    }

    public static <K, V> int countKeyOccurrences(Map<K, V> map, K key) {
        return Math.toIntExact(map.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .filter(check -> check.equals(key))
                .count());
    }

    public static <K, V> int countValueOccurrences(Map<K, V> map, V value) {
        return Math.toIntExact(map.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(check -> check.equals(value))
                .count());
    }

    public static <K, V> Map<V, List<K>> reverseMap(Map<K, V> map) {
        Map<V, List<K>> newMap = new HashMap<>();
        getMapOfParent(map, newMap);
        map.forEach((key, value) -> {
            newMap.putIfAbsent(value, new ArrayList<>());
            newMap.get(value).add(key);
        });
        return newMap;
    }

    /**
     * Gets a list of all Keys of a map.
     *
     * @param ofMap The map to get all keys from.
     * @param <T>   The type of the keys.
     * @return A list which contains all keys from the map.
     */
    public static <T> List<T> getAllKeys(Map<T, ?> ofMap) {
        List<T> val = new ArrayList<>();

        for (Map.Entry<T, ?> entry : ofMap.entrySet()) {
            val.add(entry.getKey());
        }

        return Collections.unmodifiableList(val);
    }

    /**
     * Gets a list of all Values of a map.
     *
     * @param ofMap The map to get all values from.
     * @param <T>   The type of the values.
     * @return A list which contains all values from the map.
     */
    public static <T> List<T> getAllValues(Map<?, T> ofMap) {
        List<T> val = new ArrayList<>();

        for (Map.Entry<?, T> entry : ofMap.entrySet()) {
            val.add(entry.getValue());
        }

        return Collections.unmodifiableList(val);
    }

    /**
     * Reformats a {@link TreeMap} after the provided Functions, then returns the new map and overwrites the provided
     * {@code outputMapPointer} with the new map.
     * This method requires an additional comparator to be attached to the output TreeMap.
     *
     * @param map                   The map to reformat.
     * @param keyMapper             The function to apply to every key in the map.
     * @param valueMapper           The function to apply to every value in the map.
     * @param replacementComparator The new comparator to be used by the map.
     * @param <iK>                  Input map Key type.
     * @param <iV>                  Input map Value type.
     * @param <oK>                  Output map Key type.
     * @param <oV>                  Output map Value type.
     * @param <iMap>                Type variable for the input map.
     * @param <oMap>                Type variable for the output map.
     * @return The pointer to the new map.
     */
    public static <iK, iV, oK, oV, iMap extends TreeMap<iK, iV>, oMap extends TreeMap<oK, oV>> oMap reformat(
            iMap map,
            Function<iK, oK> keyMapper,
            Function<iV, oV> valueMapper,
            Comparator<oK> replacementComparator) {
        oMap reformat = reformat(map, null, keyMapper, valueMapper);
        oMap newMap = (oMap) new TreeMap<oK, oV>(replacementComparator);
        newMap.putAll(reformat);
        return newMap;
    }

    /**
     * Reformats a {@link TreeMap} after the provided Functions, then returns the new map and overwrites the provided
     * {@code outputMapPointer} with the new map.
     * This method requires an additional comparator to be attached to the output TreeMap.
     *
     * @param map                   The map to reformat.
     * @param outputMapPointer      The output map pointer. Gets overwritten with the output map. May be {@code null}.
     * @param keyMapper             The function to apply to every key in the map.
     * @param valueMapper           The function to apply to every value in the map.
     * @param replacementComparator The new comparator to be used by the map.
     * @param <iK>                  Input map Key type.
     * @param <iV>                  Input map Value type.
     * @param <oK>                  Output map Key type.
     * @param <oV>                  Output map Value type.
     * @param <iMap>                Type variable for the input map.
     * @param <oMap>                Type variable for the output map.
     * @return The pointer to the new map.
     */
    public static <iK, iV, oK, oV, iMap extends TreeMap<iK, iV>, oMap extends TreeMap<oK, oV>> oMap reformat(
            iMap map,
            oMap outputMapPointer,
            Function<iK, oK> keyMapper,
            Function<iV, oV> valueMapper,
            Comparator<oK> replacementComparator) {
        oMap reformat = reformat(map, outputMapPointer, keyMapper, valueMapper);
        outputMapPointer = (oMap) new TreeMap<oK, oV>(replacementComparator);
        outputMapPointer.putAll(reformat);
        return outputMapPointer;
    }

    /**
     * Reformats a map after the provided Functions, then returns the new map.
     * This method is an overloaded version of {@link #reformat(Map, Map, Function, Function)}, but
     * with {@code null} as outputMapPointer.
     * When trying to reformat a {@link TreeMap} including its keys, please use
     * {@link #reformat(TreeMap, Function, Function, Comparator)}, as that method will also set the comparator
     * for the new map.
     *
     * @param map         The map to reformat.
     * @param keyMapper   The function to apply to every key in the map.
     * @param valueMapper The function to apply to every value in the map.
     * @param <iK>        Input map Key type.
     * @param <iV>        Input map Value type.
     * @param <oK>        Output map Key type.
     * @param <oV>        Output map Value type.
     * @param <iMap>      Type variable for the input map.
     * @param <oMap>      Type variable for the output map.
     * @return The pointer to the new map.
     * @implNote The returned map always conforms to the given map supertype. See {@link #getMapOfParent(Map, Map)}.
     * @see #reformat(Map, Map, Function, Function)
     */
    public static <iK, iV, oK, oV, iMap extends Map<iK, iV>, oMap extends Map<oK, oV>> oMap reformat(
            iMap map,
            Function<iK, oK> keyMapper,
            Function<iV, oV> valueMapper) {
        return reformat(map, null, keyMapper, valueMapper);
    }

    /**
     * Reformats a map after the provided Functions, then returns the new map and overwrites the provided
     * {@code outputMapPointer} with the new map.
     * When trying to reformat a {@link TreeMap} including its keys, consider using
     * {@link #reformat(TreeMap, TreeMap, Function, Function, Comparator)}, as that method will also set the
     * comparator for the new map. This method will try to place the old comparator in the new map, casting it
     * to conform to {@code Comparator<oK>}.
     *
     * @param map              The map to reformat.
     * @param outputMapPointer The output map pointer. Gets overwritten with the output map. May be {@code null}.
     * @param keyMapper        The function to apply to every key in the map.
     * @param valueMapper      The function to apply to every value in the map.
     * @param <iK>             Input map Key type.
     * @param <iV>             Input map Value type.
     * @param <oK>             Output map Key type.
     * @param <oV>             Output map Value type.
     * @param <iMap>           Type variable for the input map.
     * @param <oMap>           Type variable for the output map.
     * @return The pointer to the new map.
     * @throws ClassCastException If the map is a TreeMap whose comparator can't be {@code Comparator<\? super oK>}.
     * @implNote The returned map always conforms to the given map supertype. See {@link #getMapOfParent(Map, Map)}.
     */
    public static <iK, iV, oK, oV, iMap extends Map<iK, iV>, oMap extends Map<oK, oV>> oMap reformat(
            iMap map,
            oMap outputMapPointer,
            Function<iK, oK> keyMapper,
            Function<iV, oV> valueMapper) {
        Comparator<iK> comparator = (map instanceof TreeMap) ? ((TreeMap) map).comparator() : null;
        oMap newMap;
        newMap = (oMap) Objects.requireNonNullElse(outputMapPointer, new HashMap<oK, oV>());
        newMap = (oMap) getMapOfParent(map, newMap);
        for (Map.Entry<iK, iV> entry : map.entrySet()) {
            newMap.put(
                    keyMapper.apply(entry.getKey()),
                    valueMapper.apply(entry.getValue())
            );
        }
        if (Objects.nonNull(comparator)) {
            TreeMap<oK, oV> treeMap = new TreeMap<>((Comparator<? super oK>) comparator);
            treeMap.putAll(newMap);
            return (oMap) treeMap;
        }
        return newMap;
    }

    /**
     * Creates a new parented map of the type of {@code inputMap} and injects it into {@code outputMap}.
     * This way, methods like {@link #reformat(Map, Function, Function)} can always return the correct map type.
     * The class of {@code inputMap} should always equal the class of the returned {@code outputMap}, given that
     * the class type is implemented in this method.
     * If the class "parent" type of {@code inputMap} is not implemented in this method,
     * {@code outputMap} conforms to a new {@link HashMap}.
     * The parameter {@code outputMap} is only necessary for acquiring the type variables {@code <oK>} and
     * {@code <oV>}, and gets overwritten with a new map of parent type of {@code inputMap}.
     * If the {@code inputMap} is a {@link TreeMap}, the comparator will be dropped and the returning {@link TreeMap}
     * will have no custom comparator attached. (See {@link TreeMap} -> Comparator will be {@code null}.
     *
     * @param inputMap  Input map. Required for getting the map parent type.
     * @param outputMap Output map. Provides output type variables. Should be an <b>empty</b> {@code Map<oK, oV>}.
     * @param <iK>      Input map Key type.
     * @param <iV>      Input map Value type.
     * @param <oK>      Output map Key type.
     * @param <oV>      Output map Value type.
     * @return The newly created {@code [PARENT]Map<oK, oV>}, casted down to {@link Map}.
     */
    @SuppressWarnings("ParameterCanBeLocal")
    private static <iK, iV, oK, oV> Map<oK, oV> getMapOfParent(Map<iK, iV> inputMap, Map<oK, oV> outputMap) {
        Objects.requireNonNull(outputMap);
        if (inputMap instanceof ConcurrentHashMap) {
            outputMap = new ConcurrentHashMap<>();
        } else if (inputMap instanceof TreeMap) {
            outputMap = new TreeMap<>();
        } else if (inputMap instanceof WeakHashMap) {
            outputMap = new WeakHashMap<>();
        } else if (inputMap instanceof LinkedHashMap) {
            outputMap = new LinkedHashMap<>();
        } else if (inputMap instanceof ConcurrentSkipListMap) {
            outputMap = new ConcurrentSkipListMap<>();
        } else {
            outputMap = new HashMap<>();
        }
        return outputMap;
    }
}
