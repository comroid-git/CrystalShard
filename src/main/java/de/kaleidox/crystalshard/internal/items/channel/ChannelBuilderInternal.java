package de.kaleidox.crystalshard.internal.items.channel;

import de.kaleidox.crystalshard.main.items.channel.*;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

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
        private final Server server;

        public ServerVoiceChannelBuilder(Server server) {
            this.server = server;
        }
    }

    public static class ChannelCategoryBuilder implements ChannelCategory.Builder {
        private final Server server;

        public ChannelCategoryBuilder(Server server) {
            this.server = server;
        }
    }

    public static class GroupChannelBuilder implements GroupChannel.Builder {
        public GroupChannelBuilder(User[] recipients) {
        }
    }
}
