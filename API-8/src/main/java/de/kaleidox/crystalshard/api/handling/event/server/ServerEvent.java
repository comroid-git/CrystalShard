package de.kaleidox.crystalshard.api.handling.event.server;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.event.Event;

public interface ServerEvent extends Event {
    default long getServerId() {
        return getServer().getId();
    }

    Server getServer();
}
