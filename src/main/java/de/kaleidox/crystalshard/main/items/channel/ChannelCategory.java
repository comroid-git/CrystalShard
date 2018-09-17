package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface ChannelCategory extends ServerChannel {
    default Builder BUILDER(Server server) {
        Objects.requireNonNull(server);
        return new ChannelBuilderInternal.ChannelCategoryBuilder(server);
    }
    
    @SuppressWarnings("JavaDoc")
    interface Builder {
        Builder setServer(Server server);
        
        Builder setName(String name);
        
        Builder addPermissionOverwrite(User forUser, PermissionList permissions);
        
        Builder addPermissionOverwrite(Role forRole, PermissionList permissions);
        
        /**
         * Builds and creates the ChannelCategory, if possible.
         *
         * @return A future to contain the created ChannelCategory.
         * @throws DiscordPermissionException If the bot account does not have the permission to create a channel
         *                                    category in that guild.
         */
        CompletableFuture<ChannelCategory> build() throws DiscordPermissionException;
    }
}
