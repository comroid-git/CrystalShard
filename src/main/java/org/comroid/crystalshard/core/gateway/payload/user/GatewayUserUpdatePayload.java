package org.comroid.crystalshard.core.gateway.payload.user;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayUserUpdatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayUserUpdatePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-user-update");
    public static final VarBind<UniObjectNode, DiscordBot, User, User> user
            = Root.createBind("")
            .extractAsObject()
            .andConstruct(User.Bind.Root)
            .onceEach()
            .build();

    public GatewayUserUpdatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
