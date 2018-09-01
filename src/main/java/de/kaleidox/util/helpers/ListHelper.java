package de.kaleidox.util.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * This class contains several help methods when handling lists.
 */
public class ListHelper {
    /**
     * Moves all items within a list after the given {@code distance}.
     * If an object is null or not available, it gets replaced with {@code defaultValue}.
     * All items that get moved below index {@code 0} get dropped.
     *
     * @param list         The list to move within.
     * @param distance     The distance to move. Can be negative for downwards moving.
     * @param defaultValue A supplier to provide a default item if the index is unavailable.
     * @param <T>          The type parameter of the list.
     * @return The modified list instance.
     * @implNote The given list pointer is being overwritten by the new, modified list.
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
}
