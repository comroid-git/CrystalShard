package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.IllegalThreadException;

public interface ServerVoiceChannel extends ServerChannel, VoiceChannel {
    static Builder builder() throws IllegalThreadException {
        return builder(ThreadPool.getThreadDiscord());
    }

    static Builder builder(Discord discord) {
        return InternalDelegate.newInstance(Builder.class, discord);
    }

    Updater getUpdater();

    interface Updater extends ServerChannel.Updater<Updater, ServerVoiceChannel> {
        Updater setBitrate(int bitrate);

        Updater setUserLimit(int limit);
    }

    interface Builder extends ServerChannel.Builder<Builder, ServerVoiceChannel> {
        Builder setBitrate(int bitrate);

        Builder setUserLimit(int limit);
    }
}
