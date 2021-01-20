package org.comroid.crystalshard.gateway.event.dispatch.user;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.interaction.InteractionCreateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class TypingStartEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<TypingStartEvent> TYPE
            = BASETYPE.subGroup("typing-start", TypingStartEvent::new);

    public TypingStartEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
