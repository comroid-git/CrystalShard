package de.kaleidox.crystalshard.api.handling.listener.message.pin;

import de.kaleidox.crystalshard.api.handling.event.message.pin.MessageUnpinnedEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface MessageUnpinnedListener extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, MessageAttachableListener {
    void onMessageUnpin(MessageUnpinnedEvent event);
}
