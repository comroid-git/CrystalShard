package de.kaleidox.crystalshard.api.handling.listener.server.ban;

import de.kaleidox.crystalshard.api.handling.event.server.ban.ServerBanEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerBanListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerBanAdd(ServerBanEvent event);
}
