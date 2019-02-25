package de.kaleidox.crystalshard.api.handling.listener.message.pin;

import de.kaleidox.crystalshard.api.handling.event.message.pin.MessagePinnedEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface MessagePinnedListener extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, MessageAttachableListener {
    void onMessagePin(MessagePinnedEvent event);
}
