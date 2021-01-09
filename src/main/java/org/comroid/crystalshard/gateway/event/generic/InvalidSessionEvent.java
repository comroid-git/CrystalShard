package org.comroid.crystalshard.gateway.event.generic;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class InvalidSessionEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<InvalidSessionEvent> TYPE
            = BASETYPE.rootGroup("invalid-session");
    public static final VarBind<InvalidSessionEvent, Boolean, Boolean, Boolean> IS_RESUMABLE
            = TYPE.createBind("")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();

    public InvalidSessionEvent(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }
}
