package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelType;

public class ChannelInternal implements Channel {
    private final Discord discord;
    private final ChannelType type;
    private final long id;

    public ChannelInternal(Discord discord, JsonNode data) {
        this.discord = discord;
        this.id = data.get("id").asLong();
        this.type = ChannelType.getFromId(data.get("type").asInt());
    }

    @Override
    public ChannelType getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }
}
