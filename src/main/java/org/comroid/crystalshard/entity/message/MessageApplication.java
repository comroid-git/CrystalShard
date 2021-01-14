package org.comroid.crystalshard.entity.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class MessageApplication extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<MessageApplication> TYPE
            = BASETYPE.rootGroup("message-application");

    protected MessageApplication(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.MESSAGE_APPLICATION);
    }
}
