package org.comroid.crystalshard.gateway.event.dispatch.message.reaction;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageReactionRemoveEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<MessageReactionRemoveEvent> TYPE
            = BASETYPE.subGroup("message-reaction-remove", MessageReactionRemoveEvent::new);

    public MessageReactionRemoveEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
