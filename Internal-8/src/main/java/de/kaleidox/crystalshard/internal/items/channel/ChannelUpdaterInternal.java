package de.kaleidox.crystalshard.internal.items.channel;

import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.channel.*;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.util.helpers.FutureHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ChannelUpdaterInternal {
    public static abstract class ChannelUpdater<T, R> implements Channel.Updater<T, R> {
        protected final Discord discord;
        protected final Channel channel;
        protected T superType;

        protected ChannelUpdater(Discord discord, Channel channel) {
            this.discord = discord;
            this.channel = channel;
        }

        // Override Methods
        @Override
        public Discord getDiscord() {
            return null;
        }

        protected void setSuperType(T superType) {
            this.superType = superType;
        }
    }

    public static abstract class ServerChannelUpdater<T, R> extends ChannelUpdater<T, R> implements ServerChannel.Updater<T, R> {
        protected final ChannelType type;
        protected String name;
        protected Integer position;
        protected ChannelCategory category;
        protected List<PermissionOverride> overrides;

        protected ServerChannelUpdater(Discord discord, ServerChannel channel, ChannelType type) {
            super(discord, channel);
            this.type = type;
            this.overrides = new ArrayList<>();
        }

        // Override Methods
        @Override
        public T setName(String name) {
            this.name = name;
            return superType;
        }

        @Override
        public T setPosition(int position) {
            this.position = position;
            return superType;
        }

        @Override
        public T setCategory(ChannelCategory category) {
            this.category = category;
            return superType;
        }

        @Override
        public T modifyOverrides(Consumer<List<PermissionOverride>> overrideModifier) {
            overrideModifier.accept(this.overrides);
            return superType;
        }
    }

    public static class ServerCategoryUpdater extends ServerChannelUpdater<ChannelCategory.Updater, ChannelCategory> {
        protected ServerCategoryUpdater(Discord discord, ChannelCategory channel) {
            super(discord, channel, ChannelType.GUILD_CATEGORY);
            setSuperType(this);
            this.category = null;
        }

        // Override Methods
        @Override
        public ChannelCategory.Updater setCategory(ChannelCategory category) {
            throw new UnsupportedOperationException("Cannot change the category of a category!");
        }

        @Override
        public CompletableFuture<ChannelCategory> update() {
            if (!channel.getServerOfChannel()
                    .orElseThrow(AssertionError::new)
                    .hasPermission(discord, Permission.MANAGE_CHANNELS))
                return FutureHelper.failedFuture(new DiscordPermissionException("Cannot update channel!", Permission.MANAGE_CHANNELS));
            return CoreDelegate.webRequest(ChannelCategory.class, discord)
                    .setMethod(HttpMethod.PATCH)
                    .setUri(DiscordEndpoint.CHANNEL.createUri(channel))
                    .setNode((name != null ? new Object[]{"name",
                                    name} : new Object[0]),
                            (position != null ? new Object[]{"position",
                                    position} : new Object[0]),
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

    public static class ServerTextChannelUpdater extends ServerChannelUpdater<ServerTextChannel.Updater, ServerTextChannel>
            implements ServerTextChannel.Updater {
        protected String topic;
        protected Boolean nsfw;

        protected ServerTextChannelUpdater(Discord discord, ServerTextChannel channel) {
            super(discord, channel, ChannelType.GUILD_TEXT);
            setSuperType(this);
        }

        // Override Methods
        @Override
        public ServerTextChannel.Updater setTopic(String topic) {
            this.topic = topic;
            return superType;
        }

        @Override
        public ServerTextChannel.Updater setNSFW(boolean nsfw) {
            this.nsfw = nsfw;
            return superType;
        }

        @Override
        public CompletableFuture<ServerTextChannel> update() {
            if (!channel.getServerOfChannel()
                    .orElseThrow(AssertionError::new)
                    .hasPermission(discord, Permission.MANAGE_CHANNELS))
                return FutureHelper.failedFuture(new DiscordPermissionException("Cannot update channel!", Permission.MANAGE_CHANNELS));
            return CoreDelegate.webRequest(ServerTextChannel.class, discord)
                    .setMethod(HttpMethod.PATCH)
                    .setUri(DiscordEndpoint.CHANNEL.createUri(channel))
                    .setNode((name != null ? new Object[]{"name",
                                    name} : new Object[0]),
                            (topic != null ? new Object[]{"topic",
                                    topic} : new Object[0]),
                            (position != null ? new Object[]{"position",
                                    position} : new Object[0]),
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

    public static class ServerVoiceChannelUpdater extends ServerChannelUpdater<ServerVoiceChannel.Updater, ServerVoiceChannel>
            implements ServerVoiceChannel.Updater {
        protected Integer bitrate;
        protected Integer limit;

        protected ServerVoiceChannelUpdater(Discord discord, ServerVoiceChannel channel) {
            super(discord, channel, ChannelType.GUILD_VOICE);
            setSuperType(this);
        }

        // Override Methods
        @Override
        public ServerVoiceChannel.Updater setBitrate(int bitrate) {
            this.bitrate = bitrate;
            return superType;
        }

        @Override
        public ServerVoiceChannel.Updater setUserLimit(int limit) {
            this.limit = limit;
            return superType;
        }

        @Override
        public CompletableFuture<ServerVoiceChannel> update() {
            if (!channel.getServerOfChannel()
                    .orElseThrow(AssertionError::new)
                    .hasPermission(discord, Permission.MANAGE_CHANNELS))
                return FutureHelper.failedFuture(new DiscordPermissionException("Cannot update channel!", Permission.MANAGE_CHANNELS));
            return CoreDelegate.webRequest(ServerVoiceChannel.class, discord)
                    .setMethod(HttpMethod.PATCH)
                    .setUri(DiscordEndpoint.CHANNEL.createUri(channel))
                    .setNode((name != null ? new Object[]{"name",
                                    name} : new Object[0]),
                            (position != null ? new Object[]{"position",
                                    position} : new Object[0]),
                            (bitrate != null ? new Object[]{"bitrate",
                                    bitrate} : new Object[0]),
                            (limit != null ? new Object[]{"user_limit",
                                    limit} : new Object[0]),
                            (category != null ? new Object[]{"parent_id",
                                    category.getId()} : new Object[0]),
                            "permission_overwrites",
                            overrides.stream()
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
