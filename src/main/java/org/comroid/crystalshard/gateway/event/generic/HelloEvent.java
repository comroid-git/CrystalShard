package org.comroid.crystalshard.gateway.event.generic;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class HelloEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<HelloEvent> TYPE
            = BASETYPE.subGroup("hello", HelloEvent::new);
    public static final VarBind<HelloEvent, Integer, Integer, Integer> HEARTBEAT_INTERVAL
            = TYPE.createBind("heartbeat_interval")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public final Reference<Integer> heartbeatInterval = getComputedReference(HEARTBEAT_INTERVAL);

    public HelloEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
