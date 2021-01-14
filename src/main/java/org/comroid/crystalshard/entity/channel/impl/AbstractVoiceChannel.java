package org.comroid.crystalshard.entity.channel.impl;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.VoiceChannel;
import org.comroid.uniform.node.UniObjectNode;

public abstract class AbstractVoiceChannel extends Snowflake.Abstract implements VoiceChannel {
    protected AbstractVoiceChannel(ContextualProvider context, UniObjectNode data, EntityType<? extends VoiceChannel> entityType) {
        super(context, data, entityType);
    }
}
