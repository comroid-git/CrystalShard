package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.main.Discord;

public interface ServerVoiceChannel extends ServerChannel, VoiceChannel {
    Updater getUpdater();
    
    interface Updater extends ServerChannel.Updater<Updater, ServerVoiceChannel> {
        Updater setBitrate(int bitrate);
        
        Updater setUserLimit(int limit);
    }
    
    interface Builder extends ServerChannel.Builder<Builder, ServerVoiceChannel> {
        Builder setBitrate(int bitrate);
        
        Builder setUserLimit(int limit);
    }
    
    static Builder builder() throws IllegalCallerException {
        return builder(ThreadPool.getThreadDiscord());
    }
    
    static Builder builder(Discord discord) {
        return new ChannelBuilderInternal.ServerVoiceChannelBuilder(discord);
    }
}
