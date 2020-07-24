package org.comroid.crystalshard.core.gateway.payload.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayChannelCreatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayChannelCreatePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-channel-create");
    public static final VarBind<UniObjectNode, DiscordBot, Channel, Channel> channel
            = Root.createBind("")
            .extractAsObject()
            .andConstruct(Channel.Bind.Root)
            .onceEach()
            .build();

    public Channel getChannel() {
        return requireNonNull(channel);
    }

    public GatewayChannelCreatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}