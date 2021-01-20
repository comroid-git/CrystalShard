package org.comroid.crystalshard.gateway.event;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.gateway.OpCode;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.crystalshard.gateway.event.generic.*;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum DispatchEventType implements Named, Predicate<UniNode> {
    HELLO(HelloEvent::new),
    READY(ReadyEvent::new),
    RESUMED(ResumedEvent::new),
    RECONNECT(ReconnectEvent::new),
    INVALID_SESSION(InvalidSessionEvent::new),

    CHANNEL_CREATE(ChannelCreateEvent::new),

    GUILD_CREATE(GuildCreateEvent::new)

    /* todo */;

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
