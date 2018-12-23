package de.kaleidox.crystalshard.api.handling.listener.message.generic;

import de.kaleidox.crystalshard.api.handling.event.message.generic.MessageEditEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface MessageEditListener
        extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, UserAttachableListener, MessageAttachableListener, RoleAttachableListener {
    void onMessageEdit(MessageEditEvent event);
}
