package de.kaleidox.crystalshard.main.handling.editevent;

import java.util.Set;

public interface EditEvent<T> {
    Set<EditTrait<T>> getEditTraits();
    
    default boolean hasEdited(EditTrait<T> trait) {
        return getEditTraits().contains(trait);
    }
}
