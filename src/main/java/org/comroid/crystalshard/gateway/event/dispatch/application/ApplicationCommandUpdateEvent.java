package org.comroid.crystalshard.gateway.event.dispatch.application;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class ApplicationCommandUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<ApplicationCommandUpdateEvent> TYPE
            = BASETYPE.subGroup("application-command-update");

    public ApplicationCommandUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo
    }
}
