package org.comroid.crystalshard.model.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class MessageActivity extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<MessageActivity> TYPE
            = BASETYPE.rootGroup("message-activity");

    public MessageActivity(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
