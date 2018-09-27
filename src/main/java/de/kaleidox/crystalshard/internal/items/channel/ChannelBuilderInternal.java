package de.kaleidox.crystalshard.internal.items.channel;

import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.server.interactive.InviteInternal;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.GroupChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.Invite;
import de.kaleidox.crystalshard.main.items.user.User;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static de.kaleidox.util.helpers.JsonHelper.*;

public class ChannelBuilderInternal implements Channel.Builder {
    public static class ChannelInviteBuilder implements ServerChannel.InviteBuilder {
        private final ServerChannel channel;
        private       int           maxAge    = 0;
        private       int           maxUses   = 0;
        private       boolean       temporary = false;
        private       boolean       unique    = false;
        
        public ChannelInviteBuilder(ServerChannel channel) {
            this.channel = channel;
        }
        
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
            return new WebRequest<Invite>(channel.getDiscord()).method(Method.POST)
                    .endpoint(Endpoint.Location.CHANNEL_INVITE.toEndpoint(channel))
                    .node(objectNode("max_age", maxAge, "max_uses", maxUses, "temporary", temporary, "unique", unique))
                    .execute(node -> new InviteInternal(channel.getDiscord(), node));
        }
    }
    
    public static class ServerTextChannelBuilder implements ServerTextChannel.Builder {
        private Server          server;
        private String          name;
        private String          topic;
        private boolean         nsfw;
        private ChannelCategory category;
        
        public ServerTextChannelBuilder(Server server) {
            this.server = server;
        }
        
        // Override Methods
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
        public CompletableFuture<ServerTextChannel> build() throws DiscordPermissionException {
            throw new DiscordPermissionException(""); // todo
        }
    }
    
    public static class ServerVoiceChannelBuilder implements ServerVoiceChannel.Builder {
        private Server server;
        private String name;
        private int    bitrate;
        private int    limit;
        
        public ServerVoiceChannelBuilder(Server server) {
            this.server = server;
        }
        
        // Override Methods
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
        public CompletableFuture<ServerVoiceChannel> build() throws DiscordPermissionException {
            throw new DiscordPermissionException(""); // todo
        }
    }
    
    public static class ChannelCategoryBuilder implements ChannelCategory.Builder {
        private final Server server;
        
        public ChannelCategoryBuilder(Server server) {
            this.server = server;
        }
        
        // Override Methods
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
        public CompletableFuture<ChannelCategory> build() throws DiscordPermissionException {
            throw new DiscordPermissionException(""); // todo
        }
    }
    
    public static class GroupChannelBuilder implements GroupChannel.Builder {
        private List<User> recipients;
        
        public GroupChannelBuilder(User[] recipients) {
            if (Objects.nonNull(recipients)) {
                this.recipients = List.of(recipients);
            }
        }
        
        // Override Methods
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
