package de.kaleidox.crystalshard.api.handling.listener.channel.other;

import de.kaleidox.crystalshard.api.handling.event.channel.other.TypingStartEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface TypingStartListener
        extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, RoleAttachableListener, UserAttachableListener {
    void onTypingStart(TypingStartEvent event);
}
