package de.kaleidox.crystalshard.main.event.server;

import de.kaleidox.crystalshard.main.items.server.Server;

public interface ServerEvent {
    Server getServer();

    default long getServerId() {
        return getServer().getId();
    }
}
