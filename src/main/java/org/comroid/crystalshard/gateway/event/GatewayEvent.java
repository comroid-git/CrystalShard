package org.comroid.crystalshard.gateway.event;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public abstract class GatewayEvent extends AbstractDataContainer {
    public static final GroupBind<GatewayEvent> BASETYPE
            = AbstractDataContainer.BASETYPE.subGroup("gateway-event");

    protected GatewayEvent(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
