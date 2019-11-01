package de.comroid.crystalshard.api.entity.channel;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.model.channel.ChannelType;

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
        return Adapter.create(Builder.class, guild);
    }

    interface Builder extends GuildChannel.Builder<GuildStoreChannel, GuildStoreChannel.Builder> {
    }

    interface Updater extends GuildChannel.Updater<GuildStoreChannel, GuildStoreChannel.Updater> {
    }
}
