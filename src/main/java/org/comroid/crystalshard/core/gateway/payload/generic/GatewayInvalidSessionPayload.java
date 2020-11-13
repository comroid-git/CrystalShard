package org.comroid.crystalshard.core.gateway.payload.generic;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.uniform.ValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayInvalidSessionPayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayInvalidSessionPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-invalid-session");
    public static final VarBind<Object, Boolean, Boolean, Boolean> Resumable
            = Root.createBind("")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();

    public boolean isResumable() {
        return requireNonNull(Resumable);
    }

    public GatewayInvalidSessionPayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
