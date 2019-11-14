package de.comroid.crystalshard.api.entity.channel;

import java.util.Optional;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.model.channel.ChannelType;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;

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
        return wrapBindingValue(JSON.USER_LIMIT);
    }

    interface JSON extends GuildChannel.JSON, VoiceChannel.JSON {
        JSONBinding.OneStage<Integer> USER_LIMIT = identity("user_limit", JSONObject::getInteger);
    }

    Updater createUpdater();

    static Builder builder(Guild guild) {
        return Adapter.require(Builder.class, guild);
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
