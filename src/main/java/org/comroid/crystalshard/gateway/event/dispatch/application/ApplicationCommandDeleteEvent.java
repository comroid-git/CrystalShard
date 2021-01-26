package org.comroid.crystalshard.gateway.event.dispatch.application;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class ApplicationCommandDeleteEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<ApplicationCommandDeleteEvent> TYPE
            = BASETYPE.subGroup("application-command-delete");

    public ApplicationCommandDeleteEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo
    }
}
