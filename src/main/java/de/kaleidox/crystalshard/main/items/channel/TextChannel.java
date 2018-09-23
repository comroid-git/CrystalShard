package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.core.net.request.Endpoint;
import de.kaleidox.crystalshard.core.net.request.Method;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.internal.items.channel.PrivateTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.util.CompletableFutureExtended;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public interface TextChannel extends Channel, MessageReciever {
    String getTopic();
    
    boolean isNsfw();
    
    default void attachMessageCreateListener(MessageCreateListener listener) {
        attachListener(listener);
    }
    
    interface Updater extends Channel.Updater {
        Updater setTopic(String topic);
        
        Updater setNsfw(boolean nsfw);
        
        Updater setParent(ChannelCategory category);
    }
    
    // Static members
    // Static membe
    static CompletableFuture<TextChannel> of(Discord discord, long id) {
        CompletableFuture<TextChannel> future;
        
        future = discord.getChannelById(id)
                .filter(channel -> channel.canCastTo(TextChannel.class))
                .map(TextChannel.class::cast)
                .map(CompletableFutureExtended::completedFuture)
                .orElseGet(() -> new WebRequest<TextChannel>(discord).method(Method.GET)
                        .endpoint(Endpoint.of(Endpoint.Location.CHANNEL, id))
                        .execute(node -> {
                            if (node.has("guild_id")) {
                                return ServerTextChannelInternal.getInstance(discord, id)
                                        .toTextChannel()
                                        .orElseThrow(AssertionError::new);
                            } else if (node.has("recipients")) {
                                return PrivateTextChannelInternal.getInstance(discord, id)
                                        .toTextChannel()
                                        .orElseThrow(AssertionError::new);
                            }
                            throw new NoSuchElementException("Could not create TextChannel. ID: " + id);
                        }));
        
        return future;
    }
}
