package de.kaleidox.crystalshard.api.entity.channel;

import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.CoreInvoker;
import de.kaleidox.crystalshard.Injector;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;

public interface ServerVoiceChannel extends ServerChannel, VoiceChannel {
    Updater getUpdater();

    static CompletableFuture<ServerVoiceChannel> fromID(long id) throws IllegalThreadException {
        return fromID(ThreadPool.getThreadDiscord(), id);
    }

    static CompletableFuture<ServerVoiceChannel> fromID(Discord discord, long id) {
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

    interface Updater extends ServerChannel.Updater<Updater, ServerVoiceChannel> {
        Updater setBitrate(int bitrate);

        Updater setUserLimit(int limit);
    }

    interface Builder extends ServerChannel.Builder<Builder, ServerVoiceChannel> {
        Builder setBitrate(int bitrate);

        Builder setUserLimit(int limit);
    }
}
