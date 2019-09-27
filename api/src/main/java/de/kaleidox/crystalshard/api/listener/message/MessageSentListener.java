package de.kaleidox.crystalshard.api.listener.message;

import de.kaleidox.crystalshard.api.event.message.MessageSentEvent;
import de.kaleidox.crystalshard.api.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.listener.guild.GuildAttachableListener;
import de.kaleidox.crystalshard.api.listener.model.ListenerManager;
import de.kaleidox.crystalshard.api.listener.role.RoleAttachableListener;
import de.kaleidox.crystalshard.api.listener.user.UserAttachableListener;

public interface MessageSentListener extends
        DiscordAttachableListener<MessageSentEvent>,
        GuildAttachableListener<MessageSentEvent>,
        ChannelAttachableListener<MessageSentEvent>,
        RoleAttachableListener<MessageSentEvent>,
        UserAttachableListener<MessageSentEvent> {
    interface Manager extends ListenerManager<MessageSentListener> {
    }
}
