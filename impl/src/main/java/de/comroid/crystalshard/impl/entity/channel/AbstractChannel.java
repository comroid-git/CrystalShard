package de.comroid.crystalshard.impl.entity.channel;

import de.comroid.crystalshard.abstraction.listener.AbstractListenerAttachable;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.listener.channel.ChannelAttachableListener;
import de.comroid.crystalshard.api.model.channel.ChannelType;
import de.comroid.crystalshard.core.annotation.JsonData;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractChannel<Self extends AbstractChannel<Self>>
        extends AbstractListenerAttachable<ChannelAttachableListener, Self>
        implements Channel {
    protected @JsonData("type") ChannelType type;

    protected AbstractChannel(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override
    public ChannelType getChannelType() {
        return type;
    }
}
