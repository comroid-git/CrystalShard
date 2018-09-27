package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import java.util.Collection;

public interface TextChannel extends Channel, MessageReciever {
    Collection<Message> getPinnedMessages();
    
    default void attachMessageCreateListener(MessageCreateListener listener) {
        attachListener(listener);
    }
    
    interface Updater extends Channel.Updater {
        Updater setParent(ChannelCategory category);
    }
}
