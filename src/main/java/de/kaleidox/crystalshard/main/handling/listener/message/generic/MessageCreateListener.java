package de.kaleidox.crystalshard.main.handling.listener.message.generic;

import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface MessageCreateListener
        extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, RoleAttachableListener,
        UserAttachableListener {
    void onMessageCreate(MessageCreateEvent event);
}
