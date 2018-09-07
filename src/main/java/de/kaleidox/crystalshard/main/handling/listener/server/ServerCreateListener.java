package de.kaleidox.crystalshard.main.handling.listener.server;

import de.kaleidox.crystalshard.main.handling.event.server.ServerCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.PreAttachableListener;

@FunctionalInterface
public interface ServerCreateListener extends DiscordAttachableListener, PreAttachableListener {
    void onServerCreate(ServerCreateEvent event);
}
