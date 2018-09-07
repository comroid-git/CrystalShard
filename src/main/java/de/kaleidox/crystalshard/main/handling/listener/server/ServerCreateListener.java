package de.kaleidox.crystalshard.main.handling.listener.server;

import de.kaleidox.crystalshard.main.handling.event.server.ServerCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;

@FunctionalInterface
public interface ServerCreateListener extends DiscordAttachableListener {
    void onServerCreate(ServerCreateEvent event);
}
