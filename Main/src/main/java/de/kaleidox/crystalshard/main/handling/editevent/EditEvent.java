package de.kaleidox.crystalshard.main.handling.editevent;

import java.util.Set;

public interface EditEvent<T> {
    default boolean hasEdited(EditTrait<T> trait) {
        return getEditTraits().contains(trait);
    }

    Set<EditTrait<T>> getEditTraits();
}
