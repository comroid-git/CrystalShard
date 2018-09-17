package de.kaleidox.crystalshard.main.handling.listener.message.generic;

import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageEditEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface MessageEditListener
        extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, UserAttachableListener,
        MessageAttachableListener {
    void onMessageEdit(MessageEditEvent event);
}
