package org.comroid.crystalshard.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Util {
    /**
     * Search for an item within a list without having to modify the list. This way, you provide a Function to convert
     * the list type of item into the criteria
     * type of item.
     *
     * @param list              The list to search in.
     * @param criteria          An item to search for. Uses {@link Object#equals(Object)}.
     * @param criteriaExtractor A function to modify each entry of the list to fit the search criteria.
     * @param <A>               The type of the items in the list.
     * @param <B>               The type of the criteria to search for.
     *
     * @return An Optional that may contain the found item.
     */
    public static <A, B> Optional<A> findComplex(List<A> list, B criteria, Function<A, B> criteriaExtractor) {
        for (A item : list) {
            if (criteriaExtractor.apply(item)
                    .equals(criteria)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    /**
     * Creates a List of Lists of every {@code OF} items whithin the list {@code of}.
     *
     * @param every How many items to contain in each sublist.
     * @param of    The source list.
     * @param <T>   The type of the lists.
     *
     * @return A list of lists which contain the wanted subsets based on {@code every}.
     */
    public static <T> List<List<T>> everyOfList(int every, List<T> of) {
        ArrayList<List<T>> val = new ArrayList<>();
        ArrayList<T> count = new ArrayList<>();
        int i = 0, run = 0;

        while (run != of.size()) {
            if (i == every) i = 0;

            if (i == 0) {
                count = new ArrayList<>();
                val.add(count);
            }

            count.add(of.get(run));

            i++;
            run++;
        }

        return val;
    }

    public static <T, N> JSONArray arrayNode(Collection<T> items, Function<T, N> mapper) {
        JSONArray node = new JSONArray(items.size());
        for (T item : items) node.add(mapper.apply(item));
        return node;
    }

    public static <T> JSONArray arrayNode(Collection<T> items) {
        JSONArray node = new JSONArray(items.size());
        node.addAll(items);
        return node;
    }

    public static JSONArray arrayNode(Object... items) {
        JSONArray node = new JSONArray(items.length);
        node.addAll(Arrays.asList(items));
        return node;
    }

    public static JSONObject objectNode(Object... data) {
        if (data.length == 0) return new JSONObject();
        if (data.length % 2 != 0)
            throw new IllegalArgumentException("You must provide an even amount of objects to be placed in the node.");
        JSONObject objectNode = objectNode();
        for (List<Object> pair : everyOfList(2, Arrays.asList(data))) {
            if (Objects.nonNull(pair.get(0)) && Objects.nonNull(pair.get(1))) objectNode.put(pair.get(0).toString(), pair.get(1));
            // ignore all pairs of which both sides are NULL
        }
        return objectNode;
    }

    public static boolean arrayContains(Object[] types, Object target) {
        for (Object type : types) {
            if (type.equals(target))
                return true;
        }
        
        return false;
    }
}
