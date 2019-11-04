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
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JSONBindingLocation;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.require;
import static de.comroid.crystalshard.util.model.serialization.JSONBinding.simple;

@MainAPI
@JSONBindingLocation(Invite.Trait.class)
public interface Invite extends JsonDeserializable {
    default String getInviteCode() {
        return getBindingValue(JSON.INVITE_CODE);
    }

    default Optional<Guild> getGuild() {
        return wrapBindingValue(JSON.GUILD);
    }

    default Channel getChannel() {
        return getBindingValue(JSON.CHANNEL);
    }

    default Optional<User> getTargetUser() {
        return wrapBindingValue(JSON.TARGET_USER);
    }

    default Optional<TargetType> getTargetUserType() {
        return wrapBindingValue(JSON.TARGET_USER_TYPE);
    }

    default Optional<Integer> getApproximatePresenceCount() {
        return wrapBindingValue(JSON.APPROXIMATE_PRESENCE_COUNT);
    }

    default Optional<Integer> getApproximateMemberCount() {
        return wrapBindingValue(JSON.APPROXIMATE_MEMBER_COUNT);
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

    interface JSON {
        JSONBinding.OneStage<String> INVITE_CODE = identity("code", JSONObject::getString);
        JSONBinding.TwoStage<JSONObject, Guild> GUILD = require("guild", Guild.class);
        JSONBinding.TwoStage<JSONObject, Channel> CHANNEL = require("channel", Channel.class);
        JSONBinding.TwoStage<JSONObject, User> TARGET_USER = require("target_user", User.class);
        JSONBinding.TwoStage<Integer, TargetType> TARGET_USER_TYPE = simple("target_user_type", JSONObject::getInteger, TargetType::valueOf);
        JSONBinding.OneStage<Integer> APPROXIMATE_PRESENCE_COUNT = identity("approximate_presence_count", JSONObject::getInteger);
        JSONBinding.OneStage<Integer> APPROXIMATE_MEMBER_COUNT = identity("approximate_member_count", JSONObject::getInteger);
    }

    @MainAPI
    @JSONBindingLocation(Metadata.Trait.class)
    interface Metadata extends JsonDeserializable {
        default User getInviter() {
            return getBindingValue(JSON.INVITER);
        }

        default int getUses() {
            return getBindingValue(JSON.USES);
        }

        default int getMaximumUses() {
            return getBindingValue(JSON.MAXIMUM_USES);
        }

        default Duration getMaximumAge() {
            return getBindingValue(JSON.MAXIMUM_AGE);
        }

        default boolean isTemporary() {
            return getBindingValue(JSON.TEMPORARY);
        }

        default Instant getCreatedTimestamp() {
            return getBindingValue(JSON.CREATED_TIMESTAMP);
        }

        interface JSON {
            JSONBinding.TwoStage<JSONObject, User> INVITER = require("inviter", User.class);
            JSONBinding.OneStage<Integer> USES = identity("uses", JSONObject::getInteger);
            JSONBinding.OneStage<Integer> MAXIMUM_USES = identity("max_uses", JSONObject::getInteger);
            JSONBinding.TwoStage<Integer, Duration> MAXIMUM_AGE = simple("max_age", JSONObject::getInteger, Duration::ofSeconds);
            JSONBinding.OneStage<Boolean> TEMPORARY = identity("temporary", JSONObject::getBoolean);
            JSONBinding.TwoStage<String, Instant> CREATED_TIMESTAMP = simple("created_at", JSONObject::getString, Instant::parse);
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
