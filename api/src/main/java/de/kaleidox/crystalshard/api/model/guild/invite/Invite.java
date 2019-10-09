package de.kaleidox.crystalshard.api.model.guild.invite;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.GuildChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;
import de.kaleidox.crystalshard.util.model.serialization.JsonTraits;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.identity;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.underlying;

@JsonTraits(Invite.Trait.class)
public interface Invite extends JsonDeserializable {
    default String getInviteCode() {
        return getTraitValue(Trait.INVITE_CODE);
    }

    default Optional<Guild> getGuild() {
        return wrapTraitValue(Trait.GUILD);
    }

    default Channel getChannel() {
        return getTraitValue(Trait.CHANNEL);
    }

    default Optional<User> getTargetUser() {
        return wrapTraitValue(Trait.TARGET_USER);
    }

    default Optional<TargetType> getTargetUserType() {
        return wrapTraitValue(Trait.TARGET_USER_TYPE);
    }

    default Optional<Integer> getApproximatePresenceCount() {
        return wrapTraitValue(Trait.APPROXIMATE_PRESENCE_COUNT);
    }

    default Optional<Integer> getApproximateMemberCount() {
        return wrapTraitValue(Trait.APPROXIMATE_MEMBER_COUNT);
    }

    CompletableFuture<Metadata> requestMetadata();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/invite#delete-invite")
    CompletableFuture<Void> delete();

    static Builder builder(GuildChannel channel) {
        return Adapter.create(Builder.class, channel);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/invite#get-invite")
    static CompletableFuture<Invite> requestInvite(String inviteCode) {
        return Adapter.staticOverride(Invite.class, "requestInvite", inviteCode);
    }

    interface Trait {
        JsonTrait<String, String> INVITE_CODE = identity(JsonNode::asText, "code");
        JsonTrait<JsonNode, Guild> GUILD = underlying("guild", Guild.class);
        JsonTrait<JsonNode, Channel> CHANNEL = underlying("channel", Channel.class);
        JsonTrait<JsonNode, User> TARGET_USER = underlying("target_user", User.class);
        JsonTrait<Integer, TargetType> TARGET_USER_TYPE = simple(JsonNode::asInt, "target_user_type", TargetType::valueOf);
        JsonTrait<Integer, Integer> APPROXIMATE_PRESENCE_COUNT = identity(JsonNode::asInt, "approximate_presence_count");
        JsonTrait<Integer, Integer> APPROXIMATE_MEMBER_COUNT = identity(JsonNode::asInt, "approximate_member_count");
    }

    @JsonTraits(Metadata.Trait.class)
    interface Metadata extends JsonDeserializable {
        default User getInviter() {
            return getTraitValue(Trait.INVITER);
        }

        default int getUses() {
            return getTraitValue(Trait.USES);
        }

        default int getMaximumUses() {
            return getTraitValue(Trait.MAXIMUM_USES);
        }

        default Duration getMaximumAge() {
            return getTraitValue(Trait.MAXIMUM_AGE);
        }

        default boolean isTemporary() {
            return getTraitValue(Trait.TEMPORARY);
        }

        default Instant getCreatedTimestamp() {
            return getTraitValue(Trait.CREATED_TIMESTAMP);
        }

        interface Trait {
            JsonTrait<JsonNode, User> INVITER = underlying("inviter", User.class);
            JsonTrait<Integer, Integer> USES = identity(JsonNode::asInt, "uses");
            JsonTrait<Integer, Integer> MAXIMUM_USES = identity(JsonNode::asInt, "max_uses");
            JsonTrait<Integer, Duration> MAXIMUM_AGE = simple(JsonNode::asInt, "max_age", Duration::ofSeconds);
            JsonTrait<Boolean, Boolean> TEMPORARY = identity(JsonNode::asBoolean, "temporary");
            JsonTrait<String, Instant> CREATED_TIMESTAMP = simple(JsonNode::asText, "created_at", Instant::parse);
        }
        
        default Instant expiresAt() {
            return getCreatedTimestamp().plus(getMaximumAge());
        }
    }

    @IntroducedBy(PRODUCTION)
    interface Builder {
        GuildChannel getChannel();

        Duration getMaximumAge();

        Builder setMaximumAge(long time, TimeUnit unit);

        int getMaximumUses();

        Builder setMaximumUses(int uses);

        boolean isTemporary();

        Builder setTemporary(boolean temporary);

        boolean isUnique();

        Builder setUnique(boolean unique);

        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/channel#create-channel-invite")
        CompletableFuture<Invite> build();
    }

    enum TargetType {
        STREAM(1);

        public final int value;

        TargetType(int value) {
            this.value = value;
        }

        public static @Nullable TargetType valueOf(int value) {
            for (TargetType type : values())
                if (type.value == value)
                    return type;

            return null;
        }
    }
}
