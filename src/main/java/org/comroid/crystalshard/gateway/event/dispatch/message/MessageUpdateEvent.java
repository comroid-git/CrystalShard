package org.comroid.crystalshard.gateway.event.dispatch.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class MessageUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<MessageUpdateEvent> TYPE
            = BASETYPE.subGroup("message-update", MessageUpdateEvent::new);
    public static final VarBind<MessageUpdateEvent, UniObjectNode, Message, Message> MESSAGE
            = TYPE.createBind("")
            .extractAsObject()
            .andResolve(Message::resolve)
            .build();
    public final Reference<Message> message = getComputedReference(MESSAGE);

    public Message getMessage() {
        return message.assertion();
    }

    public MessageUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
