package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface ServerVoiceChannel extends ServerChannel, Channel {
    default Builder BUILDER(Server server) {
        Objects.requireNonNull(server);
        return new ChannelBuilderInternal.ServerVoiceChannelBuilder(server);
    }

    @SuppressWarnings("JavaDoc")
    interface Builder {
        Builder setServer(Server server);

        Builder setName(String name);

        Builder setBitrate(int bitrate);

        Builder setUserlimit(int limit);

        /**
         * Builds and creates the ServerVoiceChannel, if possible.
         *
         * @return A future to contain the created ServerVoiceChannel.
         * @throws DiscordPermissionException If the bot account does not have the permission to create
         *                                    a voice channel in that guild.
         */
        CompletableFuture<ServerVoiceChannel> build();
    }
}
