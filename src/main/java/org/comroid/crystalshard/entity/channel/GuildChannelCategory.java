package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.node.UniObjectNode;

public final class GuildChannelCategory extends Snowflake.Abstract implements GuildChannel {
    @Override
    public Guild getGuild() {
        return null;
    }

    protected GuildChannelCategory(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD_CHANNEL_CATEGORY);
    }
}
