package org.comroid.crystalshard.gateway.event;

import org.comroid.api.ContextualProvider;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class HelloEvent extends GatewayEvent {
    public static final GroupBind<HelloEvent> TYPE
            = BASETYPE.rootGroup("hello");
    public static final VarBind<HelloEvent, Integer, Integer, Integer> HEARTBEAT_INTERVAL
            = TYPE.createBind("heartbeat_interval")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public final Reference<Integer> heartbeatInterval = getComputedReference(HEARTBEAT_INTERVAL);

    public HelloEvent(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
