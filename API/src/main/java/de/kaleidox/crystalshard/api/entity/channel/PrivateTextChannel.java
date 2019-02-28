package de.kaleidox.crystalshard.api.entity.channel;

import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.CoreInvoker;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;

public interface PrivateTextChannel extends PrivateChannel, TextChannel {
    static CompletableFuture<PrivateTextChannel> fromID(long id) throws IllegalThreadException {
        return fromID(ThreadPool.getThreadDiscord(), id);
    }

    static CompletableFuture<PrivateTextChannel> fromID(Discord discord, long id) {
        return CompletableFuture.supplyAsync(
                () -> CoreInvoker.INSTANCE.fromIDs(discord, CoreInvoker.EntityType.CHANNEL, id)
        );
    }
}
