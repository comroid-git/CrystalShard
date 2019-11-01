package de.comroid.crystalshard.api.listener.message;

import de.comroid.crystalshard.api.event.message.MessageSentEvent;
import de.comroid.crystalshard.api.listener.DiscordAttachableListener;
import de.comroid.crystalshard.api.listener.channel.ChannelAttachableListener;
import de.comroid.crystalshard.api.listener.guild.GuildAttachableListener;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.api.listener.role.RoleAttachableListener;
import de.comroid.crystalshard.api.listener.user.UserAttachableListener;

public interface MessageSentListener extends
        DiscordAttachableListener<MessageSentEvent>,
        GuildAttachableListener<MessageSentEvent>,
        ChannelAttachableListener<MessageSentEvent>,
        RoleAttachableListener<MessageSentEvent>,
        UserAttachableListener<MessageSentEvent> {
    interface Manager extends ListenerManager<MessageSentListener> {
    }
}
