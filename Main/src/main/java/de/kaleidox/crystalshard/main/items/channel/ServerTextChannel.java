package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.InternalDelegate;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.IllegalThreadException;

public interface ServerTextChannel extends ServerChannel, TextChannel {
    String getTopic();
    
    boolean isNsfw();
    
    Updater getUpdater();
    
    interface Updater extends ServerChannel.Updater<Updater, ServerTextChannel> {
        Updater setTopic(String topic);
        
        Updater setNSFW(boolean nsfw);
    }
    
    interface Builder extends ServerChannel.Builder<Builder, ServerTextChannel> {
        Builder setTopic(String topic);
        
        Builder setNSFW(boolean nsfw);
    }
    
    static Builder builder() throws IllegalThreadException {
        return builder(ThreadPool.getThreadDiscord());
    }
    
    static Builder builder(Discord discord) {
        return InternalDelegate.newInstance(Builder.class, discord);
    }
}
