package org.comroid.crystalshard.gateway.event.dispatch.message.reaction;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageReactionRemoveEmojiEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<MessageReactionRemoveEmojiEvent> TYPE
            = BASETYPE.subGroup("message-reaction-remove-emoji", MessageReactionRemoveEmojiEvent::new);

    public MessageReactionRemoveEmojiEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo Implement Reaction functionality
    }
}
