package de.kaleidox.crystalshard.main.handling.listener.channel;

import de.kaleidox.crystalshard.main.handling.event.channel.generic.ChannelDeleteEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ChannelDeleteListener extends DiscordAttachableListener, ServerAttachableListener,
        ChannelAttachableListener {
    void onChannelDelete(ChannelDeleteEvent event);
}
