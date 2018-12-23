package de.kaleidox.crystalshard.api.handling.listener.channel.generic;

import de.kaleidox.crystalshard.api.handling.event.channel.generic.ChannelDeleteEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ChannelDeleteListener extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener {
    void onChannelDelete(ChannelDeleteEvent event);
}
