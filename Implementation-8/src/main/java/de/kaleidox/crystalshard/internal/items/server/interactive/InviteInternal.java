package de.kaleidox.crystalshard.internal.items.server.interactive;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.Invite;
import de.kaleidox.crystalshard.main.items.server.interactive.MetaInvite;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.helpers.FutureHelper;
import de.kaleidox.util.helpers.UrlHelper;

import java.net.URL;
import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;

public class InviteInternal implements Invite {
    private final Discord discord;
    private final String code;
    private final Server guild;
    private final ServerChannel channel;
    private final int approxPresenceCount;
    private final int approxOnlineCount;

    public InviteInternal(Discord discord, JsonNode data) {
        this.discord = discord;
        this.code = data.get("code")
                .asText();
        this.guild = data.has("guild") ? discord.getServerCache()
                .getOrRequest(data.get("guild")
                        .get("id")
                        .asLong(), data.get("guild")
                        .get("id")
                        .asLong()) :
                null;
        this.channel = discord.getChannelCache()
                .getOrRequest(data.get("channel")
                        .get("id")
                        .asLong(), data.get("channel")
                        .get("id")
                        .asLong())
                .toServerChannel()
                .orElseThrow(AssertionError::new);
        this.approxPresenceCount = data.path("approximate_presence_count")
                .asInt(-1);
        this.approxOnlineCount = data.path("approximate_online_count")
                .asInt(-1);
    }

    // Override Methods
    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public String getInviteCode() {
        return code;
    }

    @Override
    public Optional<Server> getServer() {
        return Optional.ofNullable(guild);
    }

    @Override
    public ServerChannel getChannel() {
        return channel;
    }

    @Override
    public int getApproximateOnlineCount() {
        return approxOnlineCount;
    }

    @Override
    public int getApproximateMemberCount() {
        return approxPresenceCount;
    }

    @Override
    public URL getUrl() {
        return UrlHelper.require(toString());
    }

    @Override
    public CompletableFuture<Void> delete() {
        if (!channel.hasPermission(discord, Permission.MANAGE_CHANNELS))
            return FutureHelper.failedFuture(new DiscordPermissionException(
                    "Cannot delete invite.",
                    Permission.MANAGE_CHANNELS));
        return CoreInjector.webRequest(discord)
                .setMethod(HttpMethod.DELETE)
                .setUri(DiscordEndpoint.INVITE.createUri(code))
                .executeAsVoid();
    }

    @Override
    public String toString() {
        return BASE_INVITE + code;
    }

    public static class Meta extends InviteInternal implements MetaInvite {
        private final User inviter;
        private final int uses;
        private final int maxUses;
        private final int maxAge;
        private final boolean temporary;
        private final Instant createdAt;
        private final boolean revoked;

        public Meta(Discord discord, JsonNode data) {
            super(discord, data);
            this.inviter = discord.getUserCache()
                    .getOrRequest(data.get("user")
                            .get("id")
                            .asLong(), data.get("user")
                            .get("id")
                            .asLong());
            this.uses = data.get("uses")
                    .asInt();
            this.maxUses = data.get("max_uses")
                    .asInt();
            this.maxAge = data.get("max_age")
                    .asInt();
            this.temporary = data.get("temporary")
                    .asBoolean();
            this.createdAt = Instant.parse(data.get("created_at")
                    .asText());
            this.revoked = data.get("revoked")
                    .asBoolean();
        }

        // Override Methods
        @Override
        public User getInviter() {
            return inviter;
        }

        @Override
        public int getUses() {
            return uses;
        }

        @Override
        public OptionalInt getMaxUses() {
            return maxUses == 0 ? OptionalInt.empty() : OptionalInt.of(maxUses);
        }

        @Override
        public Instant expiresAt() {
            return createdAt.plusSeconds(maxAge);
        }

        @Override
        public boolean temporaryMembership() {
            return temporary;
        }

        @Override
        public Instant createdAt() {
            return createdAt;
        }

        @Override
        public boolean isRevoked() {
            return revoked;
        }
    }
}
