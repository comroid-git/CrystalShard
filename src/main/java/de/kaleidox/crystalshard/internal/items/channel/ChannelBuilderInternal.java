package de.kaleidox.crystalshard.internal.items.channel;

import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.GroupChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ChannelBuilderInternal implements Channel.Builder {
    public static class ServerTextChannelBuilder implements ServerTextChannel.Builder {
        private Server server;
        private String name;
        private String topic;
        private boolean nsfw;
        private ChannelCategory category;

        public ServerTextChannelBuilder(Server server) {
            this.server = server;
        }

        @Override
        public ServerTextChannel.Builder setServer(Server server) {
            this.server = server;
            return this;
        }

        @Override
        public ServerTextChannel.Builder setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public ServerTextChannel.Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        @Override
        public ServerTextChannel.Builder setNSFW(boolean nsfw) {
            this.nsfw = nsfw;
            return this;
        }

        @Override
        public ServerTextChannel.Builder setCategory(ChannelCategory category) {
            this.category = category;
            return this;
        }

        @Override
        public ServerTextChannel.Builder addPermissionOverwrite(User forUser, PermissionList permissions) {
            new PermissionOverrideInternal(null, forUser);
            return this; // todo
        }

        @Override
        public ServerTextChannel.Builder addPermissionOverwrite(Role forRole, PermissionList permissions) {
            return this; // todo
        }

        @Override
        public CompletableFuture<ServerTextChannel> build() {
            return null; // todo
        }
    }

    public static class ServerVoiceChannelBuilder implements ServerVoiceChannel.Builder {
        private Server server;
        private String name;
        private int bitrate;
        private int limit;

        public ServerVoiceChannelBuilder(Server server) {
            this.server = server;
        }

        @Override
        public ServerVoiceChannel.Builder setServer(Server server) {
            this.server = server;
            return this;
        }

        @Override
        public ServerVoiceChannel.Builder setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public ServerVoiceChannel.Builder setBitrate(int bitrate) {
            this.bitrate = bitrate;
            return this;
        }

        @Override
        public ServerVoiceChannel.Builder setUserlimit(int limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public CompletableFuture<ServerVoiceChannel> build() {
            return null; // todo
        }
    }

    public static class ChannelCategoryBuilder implements ChannelCategory.Builder {
        private final Server server;

        public ChannelCategoryBuilder(Server server) {
            this.server = server;
        }

        @Override
        public ChannelCategory.Builder setServer(Server server) {
            return this;
        }

        @Override
        public ChannelCategory.Builder setName(String name) {
            return this;
        }

        @Override
        public ChannelCategory.Builder addPermissionOverwrite(User forUser, PermissionList permissions) {
            return this;
        }

        @Override
        public ChannelCategory.Builder addPermissionOverwrite(Role forRole, PermissionList permissions) {
            return this;
        }

        @Override
        public CompletableFuture<ChannelCategory> build() {
            return null; // todo
        }
    }

    public static class GroupChannelBuilder implements GroupChannel.Builder {
        private List<User> recipients;

        public GroupChannelBuilder(User[] recipients) {
            if (Objects.nonNull(recipients)) {
                this.recipients = List.of(recipients);
            }
        }

        @Override
        public GroupChannel.Builder addRecipient(User user) {
            recipients.add(user);
            return this;
        }

        @Override
        public CompletableFuture<GroupChannel> build() {
            return null; // todo
        }
    }
}
