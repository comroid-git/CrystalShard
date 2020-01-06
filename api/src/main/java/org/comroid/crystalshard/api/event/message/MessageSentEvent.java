package org.comroid.crystalshard.api.event.message;

import org.comroid.crystalshard.adapter.Constructor;
import org.comroid.crystalshard.adapter.MainAPI;
import org.comroid.crystalshard.api.entity.channel.TextChannel;
import org.comroid.crystalshard.api.event.multipart.APIEvent;
import org.comroid.crystalshard.api.event.multipart.channel.ChannelEvent;
import org.comroid.crystalshard.api.event.multipart.guild.WrappedGuildEvent;
import org.comroid.crystalshard.api.event.multipart.message.MessageEvent;
import org.comroid.crystalshard.api.event.multipart.role.WrappedRolesEvent;
import org.comroid.crystalshard.api.event.multipart.user.WrappedUserEvent;
import org.comroid.crystalshard.api.event.multipart.webhook.WrappedWebhookEvent;
import org.comroid.crystalshard.core.gateway.event.MESSAGE_CREATE;

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
