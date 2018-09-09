package de.kaleidox.crystalshard.main.handling.listener.channel;

import de.kaleidox.crystalshard.main.handling.event.channel.generic.ChannelCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ChannelCreateListener extends DiscordAttachableListener, ServerAttachableListener {
    void onChannelCreate(ChannelCreateEvent event);
}
