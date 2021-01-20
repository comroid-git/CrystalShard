package org.comroid.crystalshard.gateway.event.dispatch.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public class ChannelUpdateEvent extends GatewayEvent {
    public static final GroupBind<ChannelUpdateEvent> TYPE
            = BASETYPE.subGroup("channel-update", ChannelUpdateEvent::new);
    public static final VarBind<ChannelUpdateEvent, UniObjectNode, Channel, Channel> CHANNEL
            = TYPE.createBind("")
            .extractAsObject()
            .andResolve(Channel::resolve)
            .build();
    public final Reference<Channel> channel = getComputedReference(CHANNEL);

    public Channel getChannel() {
        return channel.assertion();
    }

    public ChannelUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
