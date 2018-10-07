package de.kaleidox.crystalshard.main.handling.listener.server.generic;

import de.kaleidox.crystalshard.main.handling.event.server.generic.ServerEditEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerEditListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerEdit(ServerEditEvent event);
}
