package org.comroid.crystalshard.gateway.event.dispatch.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class ChannelDeleteEvent extends DispatchEvent {
    public static final GroupBind<ChannelDeleteEvent> TYPE
            = BASETYPE.subGroup("channel-delete", ChannelDeleteEvent::new);
    public static final VarBind<ChannelDeleteEvent, UniObjectNode, Channel, Channel> CHANNEL
            = TYPE.createBind("")
            .extractAsObject()
            .andResolve(Channel::resolve)
            .build();
    private final Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public ChannelDeleteEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        this.channel = getComputedReference(CHANNEL).assertion();
        if (!context.getCache()
                .getReference(EntityType.CHANNEL, channel.getID())
                .unset())
            throw new RuntimeException("Could not unset Cache Reference");
    }
}
