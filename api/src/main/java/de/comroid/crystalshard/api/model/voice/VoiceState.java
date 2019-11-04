package de.comroid.crystalshard.api.model.voice;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.VoiceChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.cache;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serialize;

@JsonTraits(VoiceState.Trait.class)
public interface VoiceState extends JsonDeserializable {
    default Optional<Guild> getGuild() {
        return wrapTraitValue(Trait.GUILD);
    }

    default Optional<VoiceChannel> getChannel() {
        return wrapTraitValue(Trait.CHANNEL);
    }

    default User getUser() {
        return getTraitValue(Trait.USER);
    }

    default Optional<GuildMember> getGuildMember() {
        return wrapTraitValue(Trait.MEMBER);
    }

    default String getSessionID() {
        return getTraitValue(Trait.SESSION);
    }

    default boolean isDeafened() {
        return getTraitValue(Trait.DEAFENED);
    }

    default boolean isMuted() {
        return getTraitValue(Trait.MUTED);
    }

    default boolean isSelfDeafened() {
        return getTraitValue(Trait.SELF_DEAFENED);
    }

    default boolean isSelfMuted() {
        return getTraitValue(Trait.SELF_MUTED);
    }

    default boolean isSuppressed() {
        return getTraitValue(Trait.SUPPRESSED);
    }

    interface Trait {
        JsonBinding.TwoStage<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonBinding.TwoStage<Long, VoiceChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asVoiceChannel));
        JsonBinding.TwoStage<Long, User> USER = cache("user_id", CacheManager::getUserByID);
        JsonBinding.TwoStage<JSONObject, GuildMember> MEMBER = serialize("member", GuildMember.class);
        JsonBinding.OneStage<String> SESSION = identity("session_id", JSONObject::getString);
        JsonBinding.OneStage<Boolean> DEAFENED = identity("deaf", JSONObject::getBoolean);
        JsonBinding.OneStage<Boolean> MUTED = identity("mute", JSONObject::getBoolean);
        JsonBinding.OneStage<Boolean> SELF_DEAFENED = identity("self_deaf", JSONObject::getBoolean);
        JsonBinding.OneStage<Boolean> SELF_MUTED = identity("self_mute", JSONObject::getBoolean);
        JsonBinding.OneStage<Boolean> SUPPRESSED = identity("suppress", JSONObject::getBoolean);
    }
    
    default boolean isEffectivelyDeafened() {
        return isSelfDeafened() || isDeafened();
    }
    
    default boolean isEffectivelyMuted() {
        return isSelfMuted() || isMuted();
    }
}
