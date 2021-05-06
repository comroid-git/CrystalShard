package org.comroid.crystalshard.gateway.event.generic;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class ResumedEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<ResumedEvent> TYPE
            = BASETYPE.subGroup("resumed", ResumedEvent::new);

    public ResumedEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
