package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.IllegalThreadException;

public interface ChannelCategory extends ServerChannel {
    ServerChannel.Updater getUpdater();
    
    @SuppressWarnings("JavaDoc")
    interface Builder extends ServerChannel.Builder<Builder, ChannelCategory> { }
    
// Static membe
    static Builder builder() throws IllegalThreadException {
        return builder(ThreadPool.getThreadDiscord());
    }
    
    static Builder builder(Discord discord) {
        return InternalDelegate.newInstance(Builder.class, discord);
    }
}
