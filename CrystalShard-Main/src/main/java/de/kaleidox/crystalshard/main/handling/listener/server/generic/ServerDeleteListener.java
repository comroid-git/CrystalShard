package de.kaleidox.crystalshard.main.handling.listener.server.generic;

import de.kaleidox.crystalshard.main.handling.event.server.generic.ServerDeleteEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerDeleteListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerDelete(ServerDeleteEvent event);
}
