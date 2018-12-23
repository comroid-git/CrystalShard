package de.kaleidox.crystalshard.api.entity.channel;

import de.kaleidox.crystalshard.api.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.message.MessageReciever;

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
