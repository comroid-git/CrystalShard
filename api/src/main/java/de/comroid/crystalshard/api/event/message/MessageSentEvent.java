package de.comroid.crystalshard.api.event.message;

import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.event.DiscordEvent;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.guild.GuildEvent;
import de.comroid.crystalshard.api.event.role.RoleEvent;
import de.comroid.crystalshard.api.event.user.UserEvent;
import de.comroid.crystalshard.api.listener.message.MessageSentListener;
import de.comroid.crystalshard.util.annotation.ManagedBy;

@ManagedBy(MessageSentListener.Manager.class)
public interface MessageSentEvent extends DiscordEvent, GuildEvent, ChannelEvent<TextChannel>, RoleEvent, UserEvent, MessageEvent {
}
