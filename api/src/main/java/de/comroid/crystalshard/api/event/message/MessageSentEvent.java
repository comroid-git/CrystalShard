package de.comroid.crystalshard.api.event.message;

import de.comroid.crystalshard.adapter.Constructor;
import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.event.multipart.APIEvent;
import de.comroid.crystalshard.api.event.multipart.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.multipart.guild.WrappedGuildEvent;
import de.comroid.crystalshard.api.event.multipart.message.MessageEvent;
import de.comroid.crystalshard.api.event.multipart.role.WrappedRolesEvent;
import de.comroid.crystalshard.api.event.multipart.user.WrappedUserEvent;
import de.comroid.crystalshard.api.event.multipart.webhook.WrappedWebhookEvent;
import de.comroid.crystalshard.core.gateway.event.MESSAGE_CREATE;

@MainAPI
@Constructor(MESSAGE_CREATE.class)
public interface MessageSentEvent extends
        APIEvent,
        ChannelEvent<TextChannel>,
        WrappedGuildEvent,
        MessageEvent,
        WrappedRolesEvent,
        WrappedUserEvent,
        WrappedWebhookEvent {
}
