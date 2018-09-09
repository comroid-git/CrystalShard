package de.kaleidox.crystalshard.main.handling.listener.message;

import de.kaleidox.crystalshard.main.handling.event.message.pin.MessageUnpinnedEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface MessageUnpinnedListener extends DiscordAttachableListener, ServerAttachableListener,
        ChannelAttachableListener, MessageAttachableListener {
    void onMessageUnpin(MessageUnpinnedEvent event);
}
