package de.kaleidox.crystalshard.api.model.guild.invite;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import de.kaleidox.crystalshard.adapter.Adapter;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.GuildChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.model.ApiBound;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface Invite extends ApiBound {
    String getInviteCode();

    Optional<Guild> getGuild();

    Channel getChannel();

    Optional<User> getTargetUser();

    Optional<TargetType> getTargetType();

    OptionalInt getApproximatePresenceCount();

    OptionalInt getApproximateMemberCount();

    CompletableFuture<Metadata> getMetadata();

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/invite#delete-invite")
    CompletableFuture<Void> delete();

    static Builder builder(GuildChannel channel) {
        return Adapter.create(Builder.class, channel);
    }

    @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/invite#get-invite")
    static CompletableFuture<Invite> requestInvite(String inviteCode) {
        return Adapter.staticOverride(Invite.class, "requestInvite", inviteCode);
    }

    interface Metadata {
        User getInviter();

        int getUses();

        int getMaximumUses();

        Duration getMaximumAge();

        boolean isTemporary();

        Instant getCreatedTimestamp();

        boolean isRevoked();
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
    }
}
