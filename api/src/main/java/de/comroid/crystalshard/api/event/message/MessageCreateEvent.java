package de.comroid.crystalshard.api.event.message;

import java.util.Collection;
import java.util.Collections;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.Constructor;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.event.EventHandler;
import de.comroid.crystalshard.api.event.InitializedBy;
import de.comroid.crystalshard.api.event.multipart.APIEvent;
import de.comroid.crystalshard.api.event.multipart.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.multipart.guild.WrappedGuildEvent;
import de.comroid.crystalshard.api.event.multipart.message.MessageEvent;
import de.comroid.crystalshard.api.event.multipart.role.WrappedRolesEvent;
import de.comroid.crystalshard.api.event.multipart.user.WrappedUserEvent;
import de.comroid.crystalshard.api.event.multipart.webhook.WrappedWebhookEvent;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.event.MESSAGE_CREATE;
import de.comroid.crystalshard.util.model.NonThrowingCloseable;

@Constructor(MESSAGE_CREATE.class)
@InitializedBy(MessageCreateEvent.Initializer.class)
public interface MessageCreateEvent extends
        APIEvent,
        ChannelEvent<TextChannel>,
        WrappedGuildEvent,
        MessageEvent,
        WrappedRolesEvent,
        WrappedUserEvent,
        WrappedWebhookEvent {
    enum Initializer implements EventHandler.Initializer<MessageCreateEvent> {
        INSTANCE;
// todo this needs to actually run
        @Override
        public Collection<NonThrowingCloseable> initialize(Gateway gateway, EventHandler<MessageCreateEvent> handler) {
            return Collections.singleton(gateway.listenTo(MESSAGE_CREATE.class)
                    .handle(event -> handler.submit(Adapter.require(MessageCreateEvent.class, event))));
        }
    }
}
