package de.kaleidox.crystalshard.main.handling.listener.message;

import de.kaleidox.crystalshard.main.handling.event.message.MessageCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;

@FunctionalInterface
public interface MessageCreateListener extends ChannelAttachableListener, DiscordAttachableListener {
    void onMessageCreate(MessageCreateEvent event);
}
