package de.kaleidox.crystalshard.api.handling.listener.channel.generic;

import de.kaleidox.crystalshard.api.handling.event.channel.generic.ChannelCreateEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ChannelCreateListener extends DiscordAttachableListener, ServerAttachableListener {
    void onChannelCreate(ChannelCreateEvent event);
}
