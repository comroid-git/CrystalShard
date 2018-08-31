package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.main.ChannelContainer;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.exception.UncachedItemException;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface ServerTextChannel extends ServerChannel, TextChannel {
    default Builder BUILDER(Server server) {
        Objects.requireNonNull(server);
        return new ChannelBuilderInternal.ServerTextChannelBuilder(server);
    }

    static CompletableFuture<ServerTextChannel> of(ChannelContainer in, long id) {
        CompletableFuture<ServerTextChannel> channelFuture = new CompletableFuture<>();

        if (in instanceof Server) {
            Server srv = (Server) in;
            Discord discord = srv.getDiscord();

            channelFuture = srv.getChannels()
                    .stream()
                    .filter(chl -> chl.getId() == id)
                    .peek(channel -> {
                        assert channel.canCastTo(ServerTextChannel.class);
                    })
                    .findAny()
                    .map(ServerTextChannel.class::cast)
                    .map(CompletableFuture::completedFuture)
                    .orElseGet(() -> new WebRequest<ServerTextChannel>(discord)
                            .method(Method.POST)
                            .endpoint(Endpoint.of(Endpoint.Location.CHANNEL, srv))
                            .execute(node -> new ServerTextChannelInternal(discord, srv, node)));
        } else if (in instanceof Discord) {
            Discord discord = (Discord) in;

            CompletableFuture<ServerTextChannel> finalChannelFuture = channelFuture;
            discord.getChannelById(id)
                    .map(channel -> {
                        assert channel.canCastTo(ServerTextChannel.class);
                        return (ServerTextChannel) channel;
                    })
                    .ifPresentOrElse(channelFuture::complete, () -> finalChannelFuture
                            .completeExceptionally(new UncachedItemException("Channel is not cached.")));
        } else {
            throw new IllegalArgumentException(
                    in.getClass().getSimpleName() + " is not a valid ChannelContainer!");
        }

        return channelFuture;
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
         * @throws DiscordPermissionException If the bot account does not have the permission to create
         *                                    a text channel in that guild.
         */
        CompletableFuture<ServerTextChannel> build();
    }
}
