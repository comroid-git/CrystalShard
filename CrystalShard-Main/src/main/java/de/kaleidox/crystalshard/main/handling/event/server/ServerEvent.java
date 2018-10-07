package de.kaleidox.crystalshard.main.handling.event.server;

import de.kaleidox.crystalshard.main.handling.event.Event;
import de.kaleidox.crystalshard.main.items.server.Server;

public interface ServerEvent extends Event {
    Server getServer();
    
    default long getServerId() {
        return getServer().getId();
    }
}
