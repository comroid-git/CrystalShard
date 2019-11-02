package de.comroid.crystalshard.api.entity.channel;

import java.util.Optional;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.model.channel.ChannelType;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;

public interface GuildVoiceChannel extends GuildChannel, VoiceChannel {
    @Override
    default ChannelType getChannelType() {
        return ChannelType.GUILD_VOICE;
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.GUILD_VOICE_CHANNEL;
    }

    default Optional<Integer> getUserLimit() {
        return wrapTraitValue(Trait.USER_LIMIT);
    }

    interface Trait extends GuildChannel.Trait, VoiceChannel.Trait {
        JsonBinding<Integer, Integer> USER_LIMIT = identity(JsonNode::asInt, "user_limit");
    }

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
