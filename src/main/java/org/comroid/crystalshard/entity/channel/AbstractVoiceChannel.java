package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.uniform.node.UniObjectNode;

abstract class AbstractVoiceChannel extends Snowflake.Abstract implements VoiceChannel {
    AbstractVoiceChannel(ContextualProvider context, UniObjectNode data, EntityType<? extends VoiceChannel> entityType) {
        super(context, data, entityType);
    }
}
