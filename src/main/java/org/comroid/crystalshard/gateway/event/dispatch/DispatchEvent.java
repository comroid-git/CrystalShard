package org.comroid.crystalshard.gateway.event.dispatch;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.jetbrains.annotations.Nullable;

public abstract class DispatchEvent extends GatewayEvent {
    protected DispatchEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
