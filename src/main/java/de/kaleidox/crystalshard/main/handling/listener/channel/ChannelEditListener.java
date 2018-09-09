package de.kaleidox.crystalshard.main.handling.listener.channel;

import de.kaleidox.crystalshard.main.handling.event.channel.generic.ChannelEditEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ChannelEditListener extends DiscordAttachableListener, ServerAttachableListener,
        ChannelAttachableListener {
    void onChannelEdit(ChannelEditEvent event);
}
