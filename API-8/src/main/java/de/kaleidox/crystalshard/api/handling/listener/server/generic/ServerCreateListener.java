package de.kaleidox.crystalshard.api.handling.listener.server.generic;

import de.kaleidox.crystalshard.api.handling.event.server.generic.ServerCreateEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.PreAttachableListener;

@FunctionalInterface
public interface ServerCreateListener extends DiscordAttachableListener, PreAttachableListener {
    void onServerCreate(ServerCreateEvent event);
}
