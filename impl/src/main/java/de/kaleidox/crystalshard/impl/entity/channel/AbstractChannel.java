package de.kaleidox.crystalshard.impl.entity.channel;

import de.kaleidox.crystalshard.abstraction.listener.AbstractListenerAttachable;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;
import de.kaleidox.crystalshard.core.annotation.JsonData;

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
