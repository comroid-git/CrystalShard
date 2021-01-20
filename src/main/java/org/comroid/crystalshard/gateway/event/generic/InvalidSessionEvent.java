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

public final class InvalidSessionEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<InvalidSessionEvent> TYPE
            = BASETYPE.subGroup("invalid-session", InvalidSessionEvent::new);
    public static final VarBind<InvalidSessionEvent, Boolean, Boolean, Boolean> IS_RESUMABLE
            = TYPE.createBind("")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public final Reference<Boolean> isResumable = getComputedReference(IS_RESUMABLE);

    public InvalidSessionEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public boolean isResumable() {
        return isResumable.assertion();
    }
}
