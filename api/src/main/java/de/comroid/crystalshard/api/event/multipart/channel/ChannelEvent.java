package de.comroid.crystalshard.api.event.multipart.channel;

import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.event.multipart.APIEvent;

public interface ChannelEvent<C extends Channel> extends APIEvent {
    C getChannel();
}
