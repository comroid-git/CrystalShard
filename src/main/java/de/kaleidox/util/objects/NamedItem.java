package de.kaleidox.util.objects;

/**
 * This class represents one item with name and Type T
 *
 * @param <T> The Type of the item.
 */
public class NamedItem<T> {
    private final String name;
    private final T item;

    /**
     * Create a new NamedItem.
     *
     * @param name The name of the item.
     * @param item The item to be stored.
     */
    public NamedItem(String name, T item) {
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "NamedItem (" + name + " with item " + item.toString() + ")";
    }
}
