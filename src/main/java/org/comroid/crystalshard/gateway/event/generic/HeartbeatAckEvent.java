package org.comroid.crystalshard.gateway.event.generic;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public class HeartbeatAckEvent extends GatewayEvent {
    public static final GroupBind<HeartbeatAckEvent> TYPE
            = BASETYPE.rootGroup("heartbeat-ack");

    public HeartbeatAckEvent(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
