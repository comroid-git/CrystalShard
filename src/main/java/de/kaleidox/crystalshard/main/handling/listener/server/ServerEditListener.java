package de.kaleidox.crystalshard.main.handling.listener.server;

import de.kaleidox.crystalshard.main.handling.event.server.generic.ServerEditEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;

@FunctionalInterface
public interface ServerEditListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerEdit(ServerEditEvent event);
}
