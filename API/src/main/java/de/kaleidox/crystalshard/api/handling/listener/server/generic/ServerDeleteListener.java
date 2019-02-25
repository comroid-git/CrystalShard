package de.kaleidox.crystalshard.api.handling.listener.server.generic;

import de.kaleidox.crystalshard.api.handling.event.server.generic.ServerDeleteEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerDeleteListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerDelete(ServerDeleteEvent event);
}
