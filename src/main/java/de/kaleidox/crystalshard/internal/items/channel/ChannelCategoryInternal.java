package de.kaleidox.crystalshard.internal.items.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.ArrayList;

public class ChannelCategoryInternal extends ArrayList<Channel> implements ChannelCategory {
    public ChannelCategoryInternal(Discord discord, Server serverInternal, JsonNode channel) {
    }
}
