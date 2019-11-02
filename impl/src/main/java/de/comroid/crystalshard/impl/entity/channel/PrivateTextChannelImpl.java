package de.comroid.crystalshard.impl.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.PrivateTextChannel;

public class PrivateTextChannelImpl extends TextChannelAbst<PrivateTextChannelImpl> implements PrivateTextChannel {
    public PrivateTextChannelImpl(Discord api, JsonNode data) {
        super(api, data);
    }
}
