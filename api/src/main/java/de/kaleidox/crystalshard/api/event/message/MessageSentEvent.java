package de.kaleidox.crystalshard.api.event.message;

import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.event.DiscordEvent;
import de.kaleidox.crystalshard.api.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.api.event.guild.GuildEvent;
import de.kaleidox.crystalshard.api.event.role.RoleEvent;
import de.kaleidox.crystalshard.api.event.user.UserEvent;
import de.kaleidox.crystalshard.api.listener.message.MessageSentListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(MessageSentListener.Manager.class)
public interface MessageSentEvent extends DiscordEvent, GuildEvent, ChannelEvent<TextChannel>, RoleEvent, UserEvent, MessageEvent {
}
