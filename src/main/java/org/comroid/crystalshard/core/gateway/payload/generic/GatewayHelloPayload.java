package org.comroid.crystalshard.core.gateway.payload.generic;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.uniform.ValueType;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

@Location(GatewayHelloPayload.class)
public class GatewayHelloPayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayHelloPayload, DiscordBot> Root
            = BaseGroup.subGroup("gateway-hello", GatewayHelloPayload.class);
    public static final VarBind<Integer, DiscordBot, Integer, Integer> HeartbeatInterval
            = Root.createBind("heartbeat_interval")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();

    public GatewayHelloPayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
