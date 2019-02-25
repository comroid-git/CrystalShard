package de.kaleidox.crystalshard.api.entity.channel;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.InternalInjector;

public interface ChannelCategory extends ServerChannel {
    ServerChannel.Updater getUpdater();

    // Static membe
    static Builder builder() throws IllegalThreadException {
        return builder(ThreadPool.getThreadDiscord());
    }

    static Builder builder(Discord discord) {
        return InternalInjector.newInstance(Builder.class, discord);
    }

    @SuppressWarnings("JavaDoc")
    interface Builder extends ServerChannel.Builder<Builder, ChannelCategory> {
    }
}
