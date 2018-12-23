package de.kaleidox.crystalshard.api.handling.listener.server.ban;

import de.kaleidox.crystalshard.api.handling.event.server.ban.ServerUnbanEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerUnbanListener extends DiscordAttachableListener, ServerAttachableListener {
    void onServerBanRemove(ServerUnbanEvent event);
}
