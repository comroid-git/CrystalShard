package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface ServerTextChannel extends ServerChannel, TextChannel {
    // Override Methods
    String getTopic();
    
    default Builder BUILDER(Server server) {
        Objects.requireNonNull(server);
        return new ChannelBuilderInternal.ServerTextChannelBuilder(server);
    }
    
    @SuppressWarnings("JavaDoc")
    interface Builder extends Channel.Builder {
        Builder setServer(Server server);
        
        Builder setName(String name);
        
        Builder setTopic(String topic);
        
        Builder setNSFW(boolean nsfw);
        
        Builder setCategory(ChannelCategory category);
        
        Builder addPermissionOverwrite(User forUser, PermissionList permissions);
        
        Builder addPermissionOverwrite(Role forRole, PermissionList permissions);
        
        /**
         * Builds and creates the ServerTextChannel, if possible.
         *
         * @return A future to contain the created ServerTextChannel.
         * @throws DiscordPermissionException If the bot account does not have the permission to create a text channel
         *                                    in that guild.
         */
        CompletableFuture<ServerTextChannel> build() throws DiscordPermissionException;
    }
}
