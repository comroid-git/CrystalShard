package de.kaleidox.crystalshard.main.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.ChannelCategoryInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerVoiceChannelInternal;
import de.kaleidox.crystalshard.main.ChannelContainer;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.UncachedItemException;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.permission.PermissionApplyable;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ServerChannel extends Channel, Nameable, PermissionApplyable {
    Server getServer();

    Optional<ChannelCategory> getCategory();

    static CompletableFuture<ServerChannel> of(ChannelContainer in, long id) {
        CompletableFuture<ServerChannel> channelFuture = new CompletableFuture<>();

        if (in instanceof Server) {
            Server srv = (Server) in;
            Discord discord = srv.getDiscord();

            channelFuture = srv.getChannels()
                    .stream()
                    .filter(chl -> chl.getId() == id)
                    .peek(channel -> {
                        assert channel.canCastTo(ServerChannel.class);
                    })
                    .findAny()
                    .map(ServerChannel.class::cast)
                    .map(CompletableFuture::completedFuture)
                    .orElseGet(() -> new WebRequest<ServerChannel>(discord)
                            .method(Method.GET)
                            .endpoint(Endpoint.of(Endpoint.Location.CHANNEL, srv))
                            .execute(node -> {
                                for (JsonNode channel : node) {
                                    if (channel.get("id").asLong() == id) {
                                        ChannelType type = ChannelType.getFromId(node.get("type").asInt(-1));
                                        switch (type) {
                                            case UNKNOWN:
                                            case DM:
                                            case GROUP_DM:
                                                throw new IllegalArgumentException("ID " + id + " is not of a ServerChannel!");
                                            case GUILD_TEXT:
                                                return new ServerTextChannelInternal(discord, srv, node);
                                            case GUILD_VOICE:
                                                return new ServerVoiceChannelInternal(discord, srv, node);
                                            case GUILD_CATEGORY:
                                                return new ChannelCategoryInternal(discord, srv, node);
                                            default:
                                                throw new AssertionError();
                                        }
                                    }
                                }
                                throw new NoSuchElementException("No Channel with ID "+id+" found!");
                            }));
        } else if (in instanceof Discord) {
            Discord discord = (Discord) in;

            CompletableFuture<ServerChannel> finalChannelFuture = channelFuture;
            discord.getChannelById(id)
                    .map(channel -> {
                        assert channel.canCastTo(ServerChannel.class);
                        return (ServerChannel) channel;
                    })
                    .ifPresentOrElse(channelFuture::complete, () -> finalChannelFuture
                            .completeExceptionally(new UncachedItemException("Channel is not cached.")));
        } else {
            throw new IllegalArgumentException(
                    in.getClass().getSimpleName() + " is not a valid ChannelContainer!");
        }

        return channelFuture;
    }
}
