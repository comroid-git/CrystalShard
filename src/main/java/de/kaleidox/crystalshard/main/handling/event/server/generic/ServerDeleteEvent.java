package de.kaleidox.crystalshard.main.handling.event.server.generic;

import de.kaleidox.crystalshard.main.handling.event.server.OptionalServerEvent;
import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;

public interface ServerDeleteEvent extends OptionalServerEvent {
    boolean gotKicked();
}
