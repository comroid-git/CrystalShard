package de.kaleidox.crystalshard.main.handling.listener.server.other;

import de.kaleidox.crystalshard.main.handling.event.server.other.ServerWebhookUpdateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerWebhookUpdateListener extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener {
    void onWebhookUpdate(ServerWebhookUpdateEvent event);
}
