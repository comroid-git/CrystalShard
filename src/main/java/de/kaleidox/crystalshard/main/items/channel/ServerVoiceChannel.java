package de.kaleidox.crystalshard.main.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerVoiceChannelInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.exception.UncachedItemException;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.ChannelContainer;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface ServerVoiceChannel extends ServerChannel, Channel {
    default Builder BUILDER(Server server) {
        Objects.requireNonNull(server);
        return new ChannelBuilderInternal.ServerVoiceChannelBuilder(server);
    }

    static CompletableFuture<ServerVoiceChannel> of(ChannelContainer in, long id) {
        if (id == -1) return CompletableFuture.completedFuture(null);
        CompletableFuture<ServerVoiceChannel> channelFuture = new CompletableFuture<>();

        if (in instanceof Server) {
            Server srv = (Server) in;
            Discord discord = srv.getDiscord();

            channelFuture = srv.getChannels()
                    .stream()
                    .filter(chl -> chl.getId() == id)
                    .peek(channel -> {
                        assert channel.canCastTo(ServerVoiceChannel.class);
                    })
                    .findAny()
                    .map(ServerVoiceChannel.class::cast)
                    .map(CompletableFuture::completedFuture)
                    .orElseGet(() -> new WebRequest<ServerVoiceChannel>(discord)
                            .method(Method.GET)
                            .endpoint(Endpoint.of(Endpoint.Location.CHANNEL, srv))
                            .execute(node -> {
                                for (JsonNode channel : node) {
                                    if (channel.get("id").asLong() == id) {
                                        return ServerVoiceChannelInternal.getInstance(discord, srv, node);
                                    }
                                }
                                throw new NoSuchElementException("No Channel with ID " + id + " found!");
                            }));
        } else if (in instanceof Discord) {
            Discord discord = (Discord) in;

            CompletableFuture<ServerVoiceChannel> finalChannelFuture = channelFuture;
            discord.getChannelById(id)
                    .map(channel -> {
                        assert channel.canCastTo(ServerVoiceChannel.class);
                        return (ServerVoiceChannel) channel;
                    })
                    .ifPresentOrElse(channelFuture::complete, () -> finalChannelFuture
                            .completeExceptionally(new UncachedItemException("Channel is not cached. ID: " + id, true)));
        } else {
            throw new IllegalArgumentException(
                    in.getClass().getSimpleName() + " is not a valid ChannelContainer!");
        }

        return channelFuture;
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
