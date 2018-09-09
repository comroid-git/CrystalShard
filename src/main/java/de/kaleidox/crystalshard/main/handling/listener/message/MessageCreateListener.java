package de.kaleidox.crystalshard.main.handling.listener.message;

import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface MessageCreateListener extends DiscordAttachableListener, ServerAttachableListener,
        ChannelAttachableListener {
    void onMessageCreate(MessageCreateEvent event);
}
