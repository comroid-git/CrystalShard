package de.kaleidox.crystalshard.api.handling.event.server.generic;

import de.kaleidox.crystalshard.api.handling.event.server.OptionalServerEvent;

public interface ServerDeleteEvent extends OptionalServerEvent {
    boolean gotKicked();
}
