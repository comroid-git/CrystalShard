package org.comroid.crystalshard.api.entity.channel;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.api.model.EntityType;
import org.comroid.crystalshard.api.entity.guild.Guild;
import org.comroid.crystalshard.api.model.channel.ChannelType;

public interface GuildStoreChannel extends GuildChannel {
    @Override
    default ChannelType getChannelType() {
        return ChannelType.GUILD_STORE;
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.GUILD_STORE_CHANNEL;
    }

    Updater createUpdater();

    @Deprecated
    static Builder builder(Guild guild) {
        return Adapter.require(Builder.class, guild);
    }

    interface Builder extends GuildChannel.Builder<GuildStoreChannel, GuildStoreChannel.Builder> {
    }

    interface Updater extends GuildChannel.Updater<GuildStoreChannel, GuildStoreChannel.Updater> {
    }
}
