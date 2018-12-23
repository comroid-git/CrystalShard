package de.kaleidox.crystalshard.api.handling.event.server;

import de.kaleidox.crystalshard.api.handling.event.Event;
import de.kaleidox.crystalshard.api.entity.server.Server;

public interface ServerEvent extends Event {
    default long getServerId() {
        return getServer().getId();
    }

    Server getServer();
}
