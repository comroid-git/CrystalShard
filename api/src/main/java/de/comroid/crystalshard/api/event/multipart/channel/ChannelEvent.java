package de.comroid.crystalshard.api.event.multipart.channel;

import java.util.function.Predicate;

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

public interface ChannelEvent<C extends Channel> extends APIEvent {
    C getChannel();
    
    static Predicate<? extends ChannelEvent> filterByChannel(final Channel channel) {
        return channelEvent -> channelEvent.getChannel() != null && channelEvent.getChannel().equals(channel);
    }
}
