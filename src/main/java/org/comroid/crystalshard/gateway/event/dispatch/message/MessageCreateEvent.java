package org.comroid.crystalshard.gateway.event.dispatch.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageCreateEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<MessageUpdateEvent> TYPE
            = BASETYPE.subGroup("message-create", MessageUpdateEvent::new);

    public MessageCreateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
