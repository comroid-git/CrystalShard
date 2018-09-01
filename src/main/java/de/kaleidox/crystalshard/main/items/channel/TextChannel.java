package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.core.net.request.Endpoint;
import de.kaleidox.crystalshard.internal.core.net.request.Method;
import de.kaleidox.crystalshard.internal.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.PrivateTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.listener.MessageCreateListener;
import de.kaleidox.util.CompletableFutureExtended;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public interface TextChannel extends Channel, MessageReciever {
    void attachMessageCreateListener(MessageCreateListener listener);

    static CompletableFuture<TextChannel> of(Discord discord, long id) {
        CompletableFuture<TextChannel> future;

        future = discord.getChannelById(id)
                .filter(channel -> channel.canCastTo(TextChannel.class))
                .map(TextChannel.class::cast)
                .map(CompletableFutureExtended::completedFuture)
                .orElseGet(() -> new WebRequest<TextChannel>(discord)
                        .method(Method.GET)
                        .endpoint(Endpoint.of(Endpoint.Location.CHANNEL, id))
                        .execute(node -> {
                            if (node.has("guild_id")) {
                                return new ServerTextChannelInternal(discord,
                                        discord.getServerById(node.get("guild_id").asLong()).get(),
                                        node).castTo(TextChannel.class).get();
                            } else if (node.has("reciepients")) {
                                return new PrivateTextChannelInternal((DiscordInternal) discord, node)
                                        .castTo(TextChannel.class).get();
                            }
                            throw new NoSuchElementException("Could not create TextChannel. ID: "+id);
                        }));

        return future;
    }
}
