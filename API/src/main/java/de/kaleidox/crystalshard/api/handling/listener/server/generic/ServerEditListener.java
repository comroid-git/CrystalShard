package de.kaleidox.crystalshard.api.handling.listener.server.generic;

import de.kaleidox.crystalshard.api.handling.event.server.generic.ServerEditEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerEditListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerEdit(ServerEditEvent event);
}
