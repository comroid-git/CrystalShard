package de.kaleidox.crystalshard.api.event.model;

import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;

public interface Event {
    ListenerAttachable[] getAffected();
}
