package de.kaleidox.crystalshard.api.listener.model;

import de.kaleidox.crystalshard.api.event.model.Event;

public interface Listener<E extends Event> {
    void onEvent(E event);
}
