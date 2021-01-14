package org.comroid.crystalshard.entity.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class MessageSticker extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<MessageSticker> TYPE
            = BASETYPE.rootGroup("message-sticker");

    protected MessageSticker(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.MESSAGE_STICKER);
    }
}
