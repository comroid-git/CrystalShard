package de.kaleidox.crystalshard.main.listener;

import de.kaleidox.crystalshard.main.event.server.ServerCreateEvent;

@FunctionalInterface
public interface ServerCreateListener {
    void onEvent(ServerCreateEvent event);
}
