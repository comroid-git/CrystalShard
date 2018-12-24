package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.ServerChannel;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.listener.channel.generic.ChannelCreateListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.channel.generic.ChannelCreateEventInternal;

/**
 * https://discordapp.com/developers/docs/topics/gateway#channel-create
 */
public class CHANNEL_CREATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Channel channel = discord.getChannelCache()
                .getOrCreate(discord, data);
        Server server = channel.toServerChannel()
                .map(ServerChannel::getServer)
                .orElse(null);

        ChannelCreateEventInternal fireEvent = new ChannelCreateEventInternal(discord, channel);

        collectListeners(ChannelCreateListener.class, discord, server).forEach(listener -> listener.onChannelCreate(fireEvent));
    }
}
