package de.kaleidox.util.helpers;

import de.kaleidox.util.objects.Difference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class contains several help methods when handling lists.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ListHelper extends CollectionHelper {
// Static membe
    /**
     * Moves all items within a list after the given {@code distance}. If an object is null or not available, it gets
     * replaced with {@code defaultValue}. All items that get moved below index {@code 0} get dropped. The given list
     * pointer is being overwritten by the new, modified list.
     *
     * @param list         The list to move within.
     * @param distance     The distance to move. Can be negative for downwards moving.
     * @param defaultValue A supplier to provide a default item if the index is unavailable.
     * @param <T>          The type parameter of the list.
     * @return The modified list instance.
     */
    public static <T> List<T> moveList(List<T> list, int distance, Supplier<T> defaultValue) {
        List<T> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.size() >= i - distance) {
                if (i + distance < 0) {
                    // drop all items under index zero
                } else {
                    // list has this index; add from i+distance to i
                    T item = list.get(i - distance);
                    if (!Objects.nonNull(item)) item = defaultValue.get();
                    newList.set(i, item);
                }
            } else {
                // list doesnt have this index; add defaultValue at i
                newList.set(i, defaultValue.get());
            }
        }
        list = newList;
        return list;
    }
    
    /**
     * Search for an item within a list without having to modify the list. This way, you provide a Function to convert
     * the list type of item into the criteria type of item.
     *
     * @param list              The list to search in.
     * @param criteria          An item to search for. Uses {@link Object#equals(Object)}.
     * @param criteriaExtractor A function to modify each entry of the list to fit the search criteria.
     * @param <A>               The type of the items in the list.
     * @param <B>               The type of the criteria to search for.
     * @return An Optional that may contain the found item.
     */
    public static <A, B> Optional<A> findComplex(List<A> list, B criteria, Function<A, B> criteriaExtractor) {
        for (A item : list) {
            if (criteriaExtractor.apply(item).equals(criteria)) {
                return Optional.of(item);
            }
        }
        
        return Optional.empty();
    }
    
    public static <T> boolean booleanOfAll(List<T> list, Function<T, Boolean> booleanFunction) {
        var ref = new Object() {
            int trues = 0;
            int falses = 0;
        };
        
        list.stream().map(booleanFunction).forEach(bool -> {
            if (bool) ref.trues++;
            else ref.falses++;
        });
        
        return (ref.trues == list.size());
    }
    
    /**
     * Creates a List of Lists of every {@code OF} items whithin the list {@code of}.
     *
     * @param every How many items to contain in each sublist.
     * @param of    The source list.
     * @param <T>   The type of the lists.
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
    
    public static <T> boolean equalContents(List<T> a, List<T> b) {
        nullChecks(a, b);
        if (a.size() != b.size()) return false;
        int matches = 0;
        
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).equals(b.get(i))) matches++;
        }
        
        return matches == a.size();
    }
    
    public static <T> boolean containsEquals(List<T> list, T item) {
        requireNoNull(list);
        for (T x : list) {
            if (x.equals(item)) return true;
        }
        return false;
    }
    
    public static <T> Difference<T> getDifference(List<T> source, List<T> target) {
        nullChecks(source, target);
        Difference.Builder<T> difBuilder = new Difference.Builder<>();
        
        for (T x : source) {
            if (!containsEquals(target, x)) difBuilder.addAdded(x);
        }
        for (T x : target) {
            if (!containsEquals(source, x)) difBuilder.addRemoved(x);
        }
        
        return difBuilder.build();
    }
}
