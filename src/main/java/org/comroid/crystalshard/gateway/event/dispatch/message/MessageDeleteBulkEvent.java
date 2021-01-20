package org.comroid.crystalshard.gateway.event.dispatch.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageDeleteBulkEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<MessageDeleteBulkEvent> TYPE
            = BASETYPE.subGroup("message-delete-bulk", MessageDeleteBulkEvent::new);

    public MessageDeleteBulkEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
