package de.kaleidox.crystalshard.api.entity.channel;

import java.util.OptionalInt;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;

public interface GuildVoiceChannel extends GuildChannel, VoiceChannel {
    @Override
    default ChannelType getChannelType() {
        return ChannelType.GUILD_VOICE;
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.GUILD_VOICE_CHANNEL;
    }

    OptionalInt getUserLimit();

    Updater createUpdater();

    static Builder builder(Guild guild) {
        return Adapter.create(Builder.class, guild);
    }

    interface Builder extends
            GuildChannel.Builder<GuildVoiceChannel, GuildVoiceChannel.Builder>,
            VoiceChannel.Builder<GuildVoiceChannel, GuildVoiceChannel.Builder> {
        int getUserLimit();

        Builder setUserLimit(int userLimit);
    }

    interface Updater extends
            GuildChannel.Updater<GuildVoiceChannel, GuildVoiceChannel.Updater>,
            VoiceChannel.Updater<GuildVoiceChannel, GuildVoiceChannel.Updater> {
        int getUserLimit();

        Updater setUserLimit(int userLimit);
    }
}
