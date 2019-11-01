package de.comroid.crystalshard.api.listener.model;

import de.comroid.crystalshard.api.event.model.Event;

public interface Listener<E extends Event> {
    void onEvent(E event);
}
