package org.comroid.crystalshard.model.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;

public final class GuildIntegration extends Snowflake.Abstract {
    protected GuildIntegration(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.INTEGRATION);
    }
}
