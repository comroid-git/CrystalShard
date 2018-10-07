package de.kaleidox.crystalshard.main.handling.listener.channel.other;

import de.kaleidox.crystalshard.main.handling.event.channel.other.TypingStartEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;

@FunctionalInterface
public interface TypingStartListener
        extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, RoleAttachableListener, UserAttachableListener {
    void onTypingStart(TypingStartEvent event);
}
