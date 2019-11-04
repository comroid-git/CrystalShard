package de.comroid.crystalshard.api.model.guild.invite;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GuildChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.serialize;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;

@MainAPI
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
        return Adapter.require(Builder.class, channel);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/invite#get-invite")
    static CompletableFuture<Invite> requestInvite(String inviteCode) {
        return Adapter.staticOverride(Invite.class, "requestInvite", inviteCode);
    }

    interface Trait {
        JsonBinding.OneStage<String> INVITE_CODE = identity("code", JSONObject::getString);
        JsonBinding.TwoStage<JSONObject, Guild> GUILD = serialize("guild", Guild.class);
        JsonBinding.TwoStage<JSONObject, Channel> CHANNEL = serialize("channel", Channel.class);
        JsonBinding.TwoStage<JSONObject, User> TARGET_USER = serialize("target_user", User.class);
        JsonBinding.TwoStage<Integer, TargetType> TARGET_USER_TYPE = simple("target_user_type", JSONObject::getInteger, TargetType::valueOf);
        JsonBinding.OneStage<Integer> APPROXIMATE_PRESENCE_COUNT = identity("approximate_presence_count", JSONObject::getInteger);
        JsonBinding.OneStage<Integer> APPROXIMATE_MEMBER_COUNT = identity("approximate_member_count", JSONObject::getInteger);
    }

    @MainAPI
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
            JsonBinding.TwoStage<JSONObject, User> INVITER = serialize("inviter", User.class);
            JsonBinding.OneStage<Integer> USES = identity("uses", JSONObject::getInteger);
            JsonBinding.OneStage<Integer> MAXIMUM_USES = identity("max_uses", JSONObject::getInteger);
            JsonBinding.TwoStage<Integer, Duration> MAXIMUM_AGE = simple("max_age", JSONObject::getInteger, Duration::ofSeconds);
            JsonBinding.OneStage<Boolean> TEMPORARY = identity("temporary", JSONObject::getBoolean);
            JsonBinding.TwoStage<String, Instant> CREATED_TIMESTAMP = simple("created_at", JSONObject::getString, Instant::parse);
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
