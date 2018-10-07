package de.kaleidox.crystalshard.main.handling.listener.server.ban;

import de.kaleidox.crystalshard.main.handling.event.server.ban.ServerUnbanEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerUnbanListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerBanRemove(ServerUnbanEvent event);
}
