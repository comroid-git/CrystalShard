package de.kaleidox.crystalshard.api.entity.channel;

import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.CoreInvoker;
import de.kaleidox.crystalshard.Injector;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;

public interface ChannelCategory extends ServerChannel {
    ServerChannel.Updater getUpdater();

    static CompletableFuture<ChannelCategory> fromID(long id) throws IllegalThreadException {
        return fromID(ThreadPool.getThreadDiscord(), id);
    }

    static CompletableFuture<ChannelCategory> fromID(Discord discord, long id) {
        return CompletableFuture.supplyAsync(
                () -> CoreInvoker.INSTANCE.fromIDs(discord, CoreInvoker.EntityType.CHANNEL, id)
        );
    }

    static Builder builder() throws IllegalThreadException {
        return builder(ThreadPool.getThreadDiscord());
    }

    static Builder builder(Discord discord) {
        return Injector.create(Builder.class, discord);
    }

    interface Builder extends ServerChannel.Builder<Builder, ChannelCategory> {
    }
}
