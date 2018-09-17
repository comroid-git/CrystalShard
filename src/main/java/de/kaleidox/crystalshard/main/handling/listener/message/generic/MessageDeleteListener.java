package de.kaleidox.crystalshard.main.handling.listener.message.generic;

import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageDeleteEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface MessageDeleteListener
        extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, UserAttachableListener,
        RoleAttachableListener, MessageAttachableListener {
    void onMessageDelete(MessageDeleteEvent event);
}
