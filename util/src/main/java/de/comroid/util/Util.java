package de.comroid.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

    public static JsonNode nodeOf(Object of) {
        if (of == null) {
            return JsonNodeFactory.instance.nullNode();
        } else if (of instanceof JsonNode) {
            return (JsonNode) of;
        } else if (of instanceof Collection) {
            //noinspection unchecked
            return arrayNode((Collection) of);
        } else if (of instanceof Stream) {
            return arrayNode(((Stream) of).toArray());
        } else if (of instanceof Integer) {
            return JsonNodeFactory.instance.numberNode((Integer) of);
        } else if (of instanceof Long) {
            return JsonNodeFactory.instance.numberNode((Long) of);
        } else if (of instanceof Double) {
            return JsonNodeFactory.instance.numberNode((Double) of);
        } else if (of instanceof String) {
            return JsonNodeFactory.instance.textNode((String) of);
        } else if (of instanceof Boolean) {
            return JsonNodeFactory.instance.booleanNode((Boolean) of);
        } else {
            return JsonNodeFactory.instance.textNode(of.toString());
        }
    }

    public static <T, N> ArrayNode arrayNode(List<T> items, Function<T, N> mapper) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.size());
        for (T item : items) node.add(nodeOf(mapper.apply(item)));
        return node;
    }

    public static <T> ArrayNode arrayNode(Collection<T> items) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.size());
        for (T item : items) node.add(nodeOf(item));
        return node;
    }

    public static ArrayNode arrayNode(Object... items) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.length);
        for (Object item : items)
            node.add(nodeOf(item));
        return node;
    }

    public static ObjectNode objectNode(Object... data) {
        if (data.length == 0) return JsonNodeFactory.instance.objectNode();
        if (data.length % 2 != 0)
            throw new IllegalArgumentException("You must provide an even amount of objects to be placed in the node.");
        ObjectNode objectNode = objectNode();
        for (List<Object> pair : everyOfList(2, Arrays.asList(data))) {
            if (Objects.nonNull(pair.get(0)) && Objects.nonNull(pair.get(1))) objectNode.set(pair.get(0)
                    .toString(), nodeOf(pair.get(1)));
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
