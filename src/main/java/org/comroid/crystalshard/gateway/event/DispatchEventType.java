package org.comroid.crystalshard.gateway.event;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Invocable;
import org.comroid.api.Named;
import org.comroid.crystalshard.gateway.OpCode;
import org.comroid.crystalshard.gateway.event.dispatch.channel.ChannelCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.crystalshard.gateway.event.generic.*;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;

import java.util.Optional;
import java.util.function.Predicate;

public enum DispatchEventType implements Named, Predicate<UniNode> {
    HELLO(HelloEvent.class),
    READY(ReadyEvent.class),
    RESUMED(ResumedEvent.class),
    RECONNECT(ReconnectEvent.class),
    INVALID_SESSION(InvalidSessionEvent.class),

    CHANNEL_CREATE(ChannelCreateEvent.class),

    GUILD_CREATE(GuildCreateEvent.class)

    /* todo */;

    private final Invocable<? extends GatewayEvent> invocable;

    @Override
    public String getName() {
        return name();
    }

    DispatchEventType(Class<? extends GatewayEvent> payloadClass) {
        this.invocable = Invocable.ofClass(payloadClass);
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
        return invocable.autoInvoke(context, data);
    }
}
