package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.internal.items.server.interactive.InviteInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.Invite;
import de.kaleidox.util.helpers.FutureHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static de.kaleidox.util.helpers.JsonHelper.objectNode;

public class ChannelBuilderInternal {
    public static class ChannelInviteBuilder implements ServerChannel.InviteBuilder {
        private final ServerChannel channel;
        private int maxAge = 0;
        private int maxUses = 0;
        private boolean temporary = false;
        private boolean unique = false;

        public ChannelInviteBuilder(ServerChannel channel) {
            this.channel = channel;
        }

        // Override Methods
        @Override
        public ServerChannel.InviteBuilder setMaxAge(int maxAge) {
            this.maxAge = maxAge;
            return this;
        }

        @Override
        public ServerChannel.InviteBuilder setMaxUses(int maxUses) {
            this.maxUses = maxUses;
            return this;
        }

        @Override
        public ServerChannel.InviteBuilder setTemporaryMembership(boolean temporary) {
            this.temporary = temporary;
            return this;
        }

        @Override
        public ServerChannel.InviteBuilder setUnique(boolean unique) {
            this.unique = unique;
            return this;
        }

        @Override
        public CompletableFuture<Invite> build() {
            return CoreInjector.webRequest(Invite.class, channel.getDiscord())
                    .setMethod(HttpMethod.POST)
                    .setUri(DiscordEndpoint.CHANNEL_INVITE.createUri(channel))
                    .setNode("max_age", maxAge, "max_uses", maxUses, "temporary", temporary, "unique", unique)
                    .executeAs(node -> new InviteInternal(channel.getDiscord(), node));
        }
    }

    public static abstract class ChannelBuilder<T, R> implements Channel.Builder<T, R> {
        protected final Discord discord;
        protected T superType;

        protected ChannelBuilder(Discord discord) {
            this.discord = discord;
        }

        @Override
        public Discord getDiscord() {
            return discord;
        }

        protected void setSuperType(T superType) {
            this.superType = superType;
        }
    }

    public static abstract class ServerChannelBuilder<T, R> extends ChannelBuilder<T, R> implements ServerChannel.Builder<T, R> {
        protected final ChannelType type;
        protected Server server;
        protected String name;
        protected ChannelCategory category;
        protected List<PermissionOverride> overrides;

        protected ServerChannelBuilder(Discord discord, ChannelType type) {
            super(discord);
            this.type = type;
            this.overrides = new ArrayList<>();
        }

        // Override Methods
        @Override
        public T setServer(Server server) {
            this.server = server;
            return superType;
        }

        @Override
        public T setName(String name) {
            this.name = name;
            return superType;
        }

        @Override
        public T setCategory(ChannelCategory category) {
            this.category = category;
            return superType;
        }

        @Override
        public T addPermissionOverride(PermissionOverride override) {
            this.overrides.add(override);
            return superType;
        }

        public JsonNode toPartialJsonNode() {
            if (name == null) throw new IllegalArgumentException("No channel name set!");
            return objectNode("name", name, "type", type.getId());
        }
    }

    public static class ServerCategoryBuilder extends ServerChannelBuilder<ChannelCategory.Builder, ChannelCategory> implements ChannelCategory.Builder {
        public ServerCategoryBuilder(Discord discord) {
            super(discord, ChannelType.GUILD_CATEGORY);
            setSuperType(this);
        }

        // Override Methods
        @Override
        public ChannelCategory.Builder setCategory(ChannelCategory category) {
            throw new UnsupportedOperationException("Cannot set a category to a category!");
        }

        @Override
        public CompletableFuture<ChannelCategory> build() {
            if (!server.hasPermission(discord, Permission.MANAGE_CHANNELS))
                return FutureHelper.failedFuture(new DiscordPermissionException(
                        "Cannot create channel!",
                        Permission.MANAGE_CHANNELS));
            if (name == null) throw new IllegalArgumentException("No channel name set!");
            return CoreInjector.webRequest(ChannelCategory.class, discord)
                    .setMethod(HttpMethod.POST)
                    .setUri(DiscordEndpoint.GUILD_CHANNEL.createUri(server))
                    .setNode("type",
                            type,
                            "name",
                            name,
                            (category != null ? new Object[]{"parent_id",
                                    category.getId()} : new Object[0]),
                            "permission_overwrites",
                            overrides.stream()
                                    .map(PermissionOverrideInternal.class::cast)
                                    .map(PermissionOverrideInternal::toJsonNode)
                                    .collect(Collectors.toList()))
                    .executeAs(node -> discord.getChannelCache()
                            .getOrCreate(discord, node)
                            .toChannelCategory()
                            .orElseThrow(AssertionError::new));
        }
    }

    public static class ServerTextChannelBuilder extends ServerChannelBuilder<ServerTextChannel.Builder, ServerTextChannel>
            implements ServerTextChannel.Builder {
        protected String topic;
        protected Boolean nsfw;

        public ServerTextChannelBuilder(Discord discord) {
            super(discord, ChannelType.GUILD_TEXT);
            setSuperType(this);
        }

        // Override Methods
        @Override
        public ServerTextChannel.Builder setTopic(String topic) {
            this.topic = topic;
            return superType;
        }

        @Override
        public ServerTextChannel.Builder setNSFW(boolean nsfw) {
            this.nsfw = nsfw;
            return superType;
        }

        @Override
        public CompletableFuture<ServerTextChannel> build() {
            if (!server.hasPermission(discord, Permission.MANAGE_CHANNELS))
                return FutureHelper.failedFuture(new DiscordPermissionException(
                        "Cannot create channel!",
                        Permission.MANAGE_CHANNELS));
            if (name == null) throw new IllegalArgumentException("No channel name set!");
            return CoreInjector.webRequest(ServerTextChannel.class, discord)
                    .setMethod(HttpMethod.POST)
                    .setUri(DiscordEndpoint.GUILD_CHANNEL.createUri(server))
                    .setNode("type",
                            type,
                            "name",
                            name,
                            (topic != null ? new Object[]{"topic",
                                    topic} : new Object[0]),
                            (nsfw != null ? new Object[]{"nsfw",
                                    nsfw} : new Object[0]),
                            (category != null ? new Object[]{"parent_id",
                                    category.getId()} : new Object[0]),
                            "permission_overwrites",
                            overrides.stream()
                                    .map(PermissionOverrideInternal.class::cast)
                                    .map(PermissionOverrideInternal::toJsonNode)
                                    .collect(Collectors.toList()))
                    .executeAs(node -> discord.getChannelCache()
                            .getOrCreate(discord, node)
                            .toServerTextChannel()
                            .orElseThrow(AssertionError::new));
        }
    }

    public static class ServerVoiceChannelBuilder extends ServerChannelBuilder<ServerVoiceChannel.Builder, ServerVoiceChannel>
            implements ServerVoiceChannel.Builder {
        protected Integer bitrate;
        protected Integer limit;

        public ServerVoiceChannelBuilder(Discord discord) {
            super(discord, ChannelType.GUILD_VOICE);
            setSuperType(this);
        }

        @Override
        public ServerVoiceChannel.Builder setBitrate(int bitrate) {
            this.bitrate = bitrate;
            return superType;
        }

        @Override
        public ServerVoiceChannel.Builder setUserLimit(int limit) {
            this.limit = limit;
            return null;
        }

        @Override
        public CompletableFuture<ServerVoiceChannel> build() {
            if (!server.hasPermission(discord, Permission.MANAGE_CHANNELS))
                return FutureHelper.failedFuture(new DiscordPermissionException(
                        "Cannot create channel!",
                        Permission.MANAGE_CHANNELS));
            if (name == null) throw new IllegalArgumentException("No channel name set!");
            return CoreInjector.webRequest(ServerVoiceChannel.class, discord)
                    .setMethod(HttpMethod.POST)
                    .setUri(DiscordEndpoint.GUILD_CHANNEL.createUri(server))
                    .setNode("type", type,
                            "name", name,
                            (bitrate != null ? new Object[]{"bitrate", bitrate} : new Object[0]),
                            (limit != null ? new Object[]{"user_limit", limit} : new Object[0]),
                            (category != null ? new Object[]{"parent_id", category.getId()} : new Object[0]),
                            "permission_overwrites", overrides.stream()
                                    .map(PermissionOverrideInternal.class::cast)
                                    .map(PermissionOverrideInternal::toJsonNode)
                                    .collect(Collectors.toList()))
                    .executeAs(node -> discord.getChannelCache()
                            .getOrCreate(discord, node)
                            .toServerVoiceChannel()
                            .orElseThrow(AssertionError::new));
        }
    }
}
