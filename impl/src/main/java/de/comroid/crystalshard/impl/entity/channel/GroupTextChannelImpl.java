package de.comroid.crystalshard.impl.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.Channel;
import de.comroid.crystalshard.api.entity.channel.GroupTextChannel;

public class GroupTextChannelImpl extends TextChannelAbst<GroupTextChannelImpl> implements GroupTextChannel {
    public GroupTextChannelImpl(Discord api, JsonNode data) {
        super(api, data);
    }
}
