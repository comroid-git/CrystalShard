package org.comroid.crystalshard.entity.webhook;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;

public final class Webhook extends Snowflake.Abstract {
    protected Webhook(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.WEBHOOK);
    }
}
