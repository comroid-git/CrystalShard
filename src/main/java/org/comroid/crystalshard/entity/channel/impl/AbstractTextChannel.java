package org.comroid.crystalshard.entity.channel.impl;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.channel.TextChannel;
import org.comroid.uniform.node.UniObjectNode;

public abstract class AbstractTextChannel extends Snowflake.Abstract implements TextChannel {
    protected AbstractTextChannel(ContextualProvider context, UniObjectNode data, EntityType<? extends TextChannel> entityType) {
        super(context, data, entityType);
    }
}
