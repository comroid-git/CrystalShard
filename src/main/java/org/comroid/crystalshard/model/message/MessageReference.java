package org.comroid.crystalshard.model.message;

import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class MessageReference extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<MessageReference> TYPE
            = BASETYPE.rootGroup("message-reference");
}
