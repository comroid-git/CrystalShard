package de.kaleidox.crystalshard.main.handling.listener.server.ban;

import de.kaleidox.crystalshard.main.handling.event.server.ban.ServerBanEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerBanListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerBanAdd(ServerBanEvent event);
}
