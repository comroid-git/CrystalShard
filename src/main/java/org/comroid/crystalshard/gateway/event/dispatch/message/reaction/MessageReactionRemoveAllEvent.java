package org.comroid.crystalshard.gateway.event.dispatch.message.reaction;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageReactionRemoveAllEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<MessageReactionRemoveAllEvent> TYPE
            = BASETYPE.subGroup("message-reaction-remove-all", MessageReactionRemoveAllEvent::new);

    public MessageReactionRemoveAllEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
