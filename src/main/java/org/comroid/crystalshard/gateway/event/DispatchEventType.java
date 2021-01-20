package org.comroid.crystalshard.gateway.event;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.gateway.OpCode;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelDeleteEvent;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelPinsUpdate;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelUpdateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.crystalshard.gateway.event.generic.*;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum DispatchEventType implements Named, Predicate<UniNode> {
    // generic
    HELLO(HelloEvent::new),
    READY(ReadyEvent::new),
    RESUMED(ResumedEvent::new),
    RECONNECT(ReconnectEvent::new),
    INVALID_SESSION(InvalidSessionEvent::new),

    // channel related
    CHANNEL_CREATE(ChannelCreateEvent::new),
    CHANNEL_UPDATE(ChannelUpdateEvent::new),
    CHANNEL_DELETE(ChannelDeleteEvent::new),

    CHANNEL_PINS_UPDATE(ChannelPinsUpdate::new),

    // guild related
    GUILD_CREATE(GuildCreateEvent::new),
    GUILD_UPDATE(),
    GUILD_DELETE(),

    GUILD_BAN_ADD(),
    GUILD_BAN_REMOVE(),

    GUILD_EMOJIS_UPDATE(),

    GUILD_INTEGRATIONS_UPDATE(),

    GUILD_MEMBER_ADD(),
    GUILD_MEMBER_UPDATE(),
    GUILD_MEMBER_REMOVE(),
    GUILD_MEMBERS_CHUNK(),

    GUILD_ROLE_CREATE(),
    GUILD_ROLE_UPDATE(),
    GUILD_ROLE_DELETE(),

    INVITE_CREATE(),
    INVITE_DELETE(),

    // message related
    MESSAGE_CREATE(),
    MESSAGE_UPDATE(),
    MESSAGE_DELETE(),
    MESSAGE_DELETE_BULK(),

    MESSAGE_REACTION_ADD(),
    MESSAGE_REACTION_REMOVE(),
    MESSAGE_REACTION_REMOVE_EMOJI(),
    MESSAGE_REACTION_REMOVE_ALL(),

    // misc
    PRESENCE_UPDATE(),
    TYPING_START(),
    USER_UPDATE(),

    // voice related
    VOICE_STATE_UPDATE(),
    VOICE_SERVER_UPDATE(),

    // webhook
    WEBHOOKS_UPDATE(),

    INTERACTION_CREATE();

    private final BiFunction<ContextualProvider, UniObjectNode, ? extends GatewayEvent> constructor;

    @Override
    public String getName() {
        return name();
    }

    DispatchEventType(BiFunction<ContextualProvider, UniObjectNode, ? extends GatewayEvent> constructor) {
        this.constructor = constructor;
    }

    public static Optional<DispatchEventType> find(UniNode data) {
        for (DispatchEventType type : values())
            if (type.test(data))
                return Optional.of(type);
        return Optional.empty();
    }

    @Override
    public boolean test(UniNode uniNode) {
        return OpCode.DISPATCH.test(uniNode) && uniNode
                .process("t")
                .map(UniNode::asString)
                .test(name()::equals);
    }

    public GatewayEvent createPayload(ContextualProvider context, UniObjectNode data) {
        return constructor.apply(context, data);
    }
}
