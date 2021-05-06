package org.comroid.crystalshard.gateway.event.generic;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class ReconnectEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<ReconnectEvent> TYPE
            = BASETYPE.subGroup("reconnect", ReconnectEvent::new);

    public ReconnectEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
