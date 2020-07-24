package org.comroid.crystalshard.core.gateway.payload.guild;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayGuildCreatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayGuildCreatePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-guild-create");
    public static final VarBind<UniObjectNode, DiscordBot, Guild, Guild> guild
            = Root.createBind("")
            .extractAsObject()
            .andConstruct(Guild.Bind.Root)
            .onceEach()
            .build();

    public GatewayGuildCreatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
