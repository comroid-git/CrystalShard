package de.kaleidox.crystalshard.main.handling.listener.message;

import de.kaleidox.crystalshard.main.handling.event.message.pin.MessagePinnedEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface MessagePinnedListener extends DiscordAttachableListener, ServerAttachableListener,
        ChannelAttachableListener, MessageAttachableListener {
    void onMessagePin(MessagePinnedEvent event);
}
