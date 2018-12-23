package de.kaleidox.crystalshard.api.handling.listener.message.generic;

import de.kaleidox.crystalshard.api.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface MessageCreateListener
        extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, RoleAttachableListener, UserAttachableListener {
    void onMessageCreate(MessageCreateEvent event);
}
