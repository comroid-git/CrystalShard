package de.kaleidox.crystalshard.api.handling.listener.channel.generic;

import de.kaleidox.crystalshard.api.handling.event.channel.generic.ChannelEditEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ChannelEditListener extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener {
    void onChannelEdit(ChannelEditEvent event);
}
