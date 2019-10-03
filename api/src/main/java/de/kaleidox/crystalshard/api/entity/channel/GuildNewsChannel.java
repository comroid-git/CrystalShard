package de.kaleidox.crystalshard.api.entity.channel;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;

public interface GuildNewsChannel extends GuildChannel {
    @Override
    default ChannelType getChannelType() {
        return ChannelType.GUILD_NEWS;
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.GUILD_NEWS_CHANNEL;
    }

    Updater createUpdater();

    @Deprecated
    static Builder builder(Guild guild) {
        return Adapter.create(Builder.class, guild);
    }

    interface Builder extends GuildChannel.Builder<GuildNewsChannel, GuildNewsChannel.Builder> {
    }

    interface Updater extends GuildChannel.Updater<GuildNewsChannel, GuildNewsChannel.Updater> {
    }
}