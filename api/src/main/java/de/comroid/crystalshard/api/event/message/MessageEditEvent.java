package de.comroid.crystalshard.api.event.message;

import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.event.DiscordEvent;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.guild.WrappedGuildEvent;
import de.comroid.crystalshard.api.event.guild.webhook.WrappedWebhookEvent;
import de.comroid.crystalshard.api.event.role.WrappedRoleEvent;
import de.comroid.crystalshard.api.event.user.WrappedUserEvent;

public interface MessageEditEvent extends
        DiscordEvent,
        WrappedGuildEvent,
        ChannelEvent<TextChannel>,
        WrappedRoleEvent,
        WrappedUserEvent,
        WrappedWebhookEvent,
        MessageEvent {
}
