package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.main.Discord;

public interface ChannelCategory extends ServerChannel {
    ServerChannel.Updater getUpdater();
    
    @SuppressWarnings("JavaDoc")
    interface Builder extends ServerChannel.Builder<Builder, ChannelCategory> { }
    
    static Builder builder() throws IllegalCallerException {
        return builder(ThreadPool.getThreadDiscord());
    }
    
    static Builder builder(Discord discord) {
        return new ChannelBuilderInternal.ServerCategoryBuilder(discord);
    }
}
