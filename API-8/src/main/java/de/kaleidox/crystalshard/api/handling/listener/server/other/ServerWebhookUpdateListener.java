package de.kaleidox.crystalshard.api.handling.listener.server.other;

import de.kaleidox.crystalshard.api.handling.event.server.other.ServerWebhookUpdateEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ServerWebhookUpdateListener extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener {
    void onWebhookUpdate(ServerWebhookUpdateEvent event);
}
