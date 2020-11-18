package org.comroid.crystalshard.entity.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;

public final class Guild extends Snowflake.Abstract {
    public static final GroupBind<Guild> TYPE = BASETYPE.rootGroup("guild");

    public Guild(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD);
    }
}
