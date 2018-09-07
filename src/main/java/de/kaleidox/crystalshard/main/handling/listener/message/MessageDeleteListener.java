package de.kaleidox.crystalshard.main.handling.listener.message;

import de.kaleidox.crystalshard.main.handling.event.message.MessageDeleteEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface MessageDeleteListener extends DiscordAttachableListener, ServerAttachableListener,
        ChannelAttachableListener, MessageAttachableListener {
    void onMessageDelete(MessageDeleteEvent event);
}
