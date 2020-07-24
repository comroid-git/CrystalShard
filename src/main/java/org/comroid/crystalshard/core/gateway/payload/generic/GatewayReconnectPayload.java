package org.comroid.crystalshard.core.gateway.payload.generic;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class GatewayReconnectPayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayReconnectPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-reconnect");

    public GatewayReconnectPayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
