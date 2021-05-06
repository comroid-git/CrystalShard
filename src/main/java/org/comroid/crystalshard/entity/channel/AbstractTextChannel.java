package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;

abstract class AbstractTextChannel extends Snowflake.Abstract implements TextChannel {
    AbstractTextChannel(ContextualProvider context, UniObjectNode data, EntityType<? extends TextChannel> entityType) {
        super(context, data, entityType);
    }
}
