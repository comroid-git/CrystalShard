package de.comroid.crystalshard.api.model.voice;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.VoiceChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTrait;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.model.serialization.JsonTrait.cache;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonTrait.underlying;

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
        JsonTrait<Long, Guild> GUILD = cache("guild_id", CacheManager::getGuildByID);
        JsonTrait<Long, VoiceChannel> CHANNEL = cache("channel_id", (cache, id) -> cache.getChannelByID(id).flatMap(Channel::asVoiceChannel));
        JsonTrait<Long, User> USER = cache("user_id", CacheManager::getUserByID);
        JsonTrait<JsonNode, GuildMember> MEMBER = underlying("member", GuildMember.class);
        JsonTrait<String, String> SESSION = identity(JsonNode::asText, "session_id");
        JsonTrait<Boolean, Boolean> DEAFENED = identity(JsonNode::asBoolean, "deaf");
        JsonTrait<Boolean, Boolean> MUTED = identity(JsonNode::asBoolean, "mute");
        JsonTrait<Boolean, Boolean> SELF_DEAFENED = identity(JsonNode::asBoolean, "self_deaf");
        JsonTrait<Boolean, Boolean> SELF_MUTED = identity(JsonNode::asBoolean, "self_mute");
        JsonTrait<Boolean, Boolean> SUPPRESSED = identity(JsonNode::asBoolean, "suppress");
    }
    
    default boolean isEffectivelyDeafened() {
        return isSelfDeafened() || isDeafened();
    }
    
    default boolean isEffectivelyMuted() {
        return isSelfMuted() || isMuted();
    }
}
