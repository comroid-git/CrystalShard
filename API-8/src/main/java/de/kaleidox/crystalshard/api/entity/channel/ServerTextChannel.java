package de.kaleidox.crystalshard.api.entity.channel;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;

public interface ServerTextChannel extends ServerChannel, TextChannel {
    String getTopic();

    boolean isNsfw();

    Updater getUpdater();

    static Builder builder() throws IllegalThreadException {
        return builder(ThreadPool.getThreadDiscord());
    }

    static Builder builder(Discord discord) {
        return InternalInjector.newInstance(Builder.class, discord);
    }

    interface Updater extends ServerChannel.Updater<Updater, ServerTextChannel> {
        Updater setTopic(String topic);

        Updater setNSFW(boolean nsfw);
    }

    interface Builder extends ServerChannel.Builder<Builder, ServerTextChannel> {
        Builder setTopic(String topic);

        Builder setNSFW(boolean nsfw);
    }
}
