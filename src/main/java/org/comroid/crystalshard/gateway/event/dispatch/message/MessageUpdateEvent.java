package org.comroid.crystalshard.gateway.event.dispatch.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageUpdateEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<MessageUpdateEvent> TYPE
            = BASETYPE.subGroup("message-update", MessageUpdateEvent::new);

    public MessageUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
