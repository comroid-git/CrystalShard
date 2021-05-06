package org.comroid.crystalshard.gateway.event.dispatch.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class ChannelCreateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<ChannelCreateEvent> TYPE
            = BASETYPE.subGroup("channel-create", ChannelCreateEvent::new);
    public static final VarBind<ChannelCreateEvent, UniObjectNode, Channel, Channel> CHANNEL
            = TYPE.createBind("")
            .extractAsObject()
            .andResolve(Channel::resolve)
            .build();
    public final Reference<Channel> channel = getComputedReference(CHANNEL);

    public Channel getChannel() {
        return channel.assertion();
    }

    public ChannelCreateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
