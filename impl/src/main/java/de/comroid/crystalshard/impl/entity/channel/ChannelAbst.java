package de.comroid.crystalshard.impl.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.abstraction.listener.AbstractListenerAttachable;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.listener.AttachableTo;

public class ChannelAbst<Self extends ChannelAbst<Self>> 
        extends AbstractListenerAttachable<AttachableTo.Channel<? extends ChannelEvent<? extends Channel>>, Self>
        implements de.comroid.crystalshard.api.entity.channel.Channel {
    protected ChannelAbst(Discord api, JsonNode data) {
        super(api, data);
    }
}
