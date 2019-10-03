package de.kaleidox.crystalshard.api.entity.channel;

import java.util.Optional;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;

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
        JsonTrait<Integer, Integer> USER_LIMIT = identity(JsonNode::asInt, "user_limit");
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
