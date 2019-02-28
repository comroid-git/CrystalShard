package de.kaleidox.crystalshard.api.entity.channel;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;

public interface ServerVoiceChannel extends ServerChannel, VoiceChannel {
    Updater getUpdater();

    static Builder builder() throws IllegalThreadException {
        return builder(ThreadPool.getThreadDiscord());
    }

    static Builder builder(Discord discord) {
        return InternalInjector.newInstance(Builder.class, discord);
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
