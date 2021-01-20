package org.comroid.crystalshard.gateway.event.dispatch.message.reaction;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.message.MessageUpdateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageReactionAddEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<MessageReactionAddEvent> TYPE
            = BASETYPE.subGroup("message-reaction-add", MessageReactionAddEvent::new);

    public MessageReactionAddEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
