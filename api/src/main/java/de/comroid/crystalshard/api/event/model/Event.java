package de.comroid.crystalshard.api.event.model;

import de.comroid.crystalshard.api.listener.model.ListenerAttachable;

public interface Event {
    ListenerAttachable[] getAffected();
}
