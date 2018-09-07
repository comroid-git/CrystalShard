package de.kaleidox.crystalshard.main.listener;

import de.kaleidox.crystalshard.main.event.message.MessageCreateEvent;

@FunctionalInterface
public interface MessageCreateListener extends DiscordAttachableListener, ChannelAttachableListener {
    void onMessageCreate(MessageCreateEvent event);
}
