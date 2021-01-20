package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;

public final class GuildChannelCategory extends Snowflake.Abstract implements GuildChannel {
    @RootBind
    public static final GroupBind<GuildChannelCategory> TYPE
            = GuildChannel.BASETYPE.subGroup("guild-channel-category");

    @Override
    public Guild getGuild() {
        return null;
    }

    GuildChannelCategory(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GUILD_CHANNEL_CATEGORY);
    }
}
