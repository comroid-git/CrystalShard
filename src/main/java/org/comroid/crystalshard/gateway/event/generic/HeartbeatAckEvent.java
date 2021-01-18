package org.comroid.crystalshard.gateway.event.generic;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class HeartbeatAckEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<HeartbeatAckEvent> TYPE
            = BASETYPE.rootGroup("heartbeat-ack");

    public HeartbeatAckEvent(ContextualProvider context) {
        super(context, null);
    }
}
