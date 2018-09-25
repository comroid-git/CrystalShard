package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;

public interface TextChannel extends Channel, MessageReciever {
    String getTopic();
    
    boolean isNsfw();
    
    default void attachMessageCreateListener(MessageCreateListener listener) {
        attachListener(listener);
    }
    
    interface Updater extends Channel.Updater {
        Updater setTopic(String topic);
        
        Updater setNsfw(boolean nsfw);
        
        Updater setParent(ChannelCategory category);
    }
}
