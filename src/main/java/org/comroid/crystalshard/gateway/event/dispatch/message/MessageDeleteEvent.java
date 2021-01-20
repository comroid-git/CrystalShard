package org.comroid.crystalshard.gateway.event.dispatch.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageDeleteEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<MessageDeleteEvent> TYPE
            = BASETYPE.subGroup("message-delete", MessageDeleteEvent::new);

    public MessageDeleteEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
