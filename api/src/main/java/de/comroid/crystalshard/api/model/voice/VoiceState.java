package de.comroid.crystalshard.api.model.voice;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.VoiceChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;

@JSONBindingLocation(VoiceState.Trait.class)
public interface VoiceState extends JsonDeserializable {
    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    default Optional<VoiceChannel> getChannel() {
        return wrapBindingValue(JSON.CHANNEL);
    }

    default User getUser() {
        return getBindingValue(JSON.USER);
    }

    default Optional<GuildMember> getGuildMember() {
        return wrapBindingValue(JSON.MEMBER);
    }

    default String getSessionID() {
        return getBindingValue(JSON.SESSION);
    }

    default boolean isDeafened() {
        return getBindingValue(JSON.DEAFENED);
    }

    default boolean isMuted() {
        return getBindingValue(JSON.MUTED);
    }

    default boolean isSelfDeafened() {
        return getBindingValue(JSON.SELF_DEAFENED);
    }

    default boolean isSelfMuted() {
        return getBindingValue(JSON.SELF_MUTED);
    }

    default boolean isSuppressed() {
        return getBindingValue(JSON.SUPPRESSED);
    }

    interface JSON {
        JSONBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JSONBinding.TwoStage<Long, VoiceChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asVoiceChannel));
        JSONBinding.TwoStage<Long, User> USER = cache("user_id", CacheManager::getUserByID);
        JSONBinding.TwoStage<JSONObject, GuildMember> MEMBER = require("member", GuildMember.class);
        JSONBinding.OneStage<String> SESSION = identity("session_id", JSONObject::getString);
        JSONBinding.OneStage<Boolean> DEAFENED = identity("deaf", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> MUTED = identity("mute", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> SELF_DEAFENED = identity("self_deaf", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> SELF_MUTED = identity("self_mute", JSONObject::getBoolean);
        JSONBinding.OneStage<Boolean> SUPPRESSED = identity("suppress", JSONObject::getBoolean);
    }
    
    default boolean isEffectivelyDeafened() {
        return isSelfDeafened() || isDeafened();
    }
    
    default boolean isEffectivelyMuted() {
        return isSelfMuted() || isMuted();
    }
}
