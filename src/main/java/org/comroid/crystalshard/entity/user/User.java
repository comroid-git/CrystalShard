package org.comroid.crystalshard.entity.user;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;

public final class User extends Snowflake.Abstract {
    public static final GroupBind<User> TYPE = BASETYPE.rootGroup("user");

    public User(ContextualProvider context, UniObjectNode data, EntityType entityType) {
        super(context, data, entityType);
    }
}
