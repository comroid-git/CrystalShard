package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.channel.generic.ChannelCreateEventInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.main.handling.listener.channel.generic.ChannelCreateListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.server.Server;

/**
 * https://discordapp.com/developers/docs/topics/gateway#channel-create
 */
public class CHANNEL_CREATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Channel channel = ChannelInternal.getInstance(discord, data);
        Server server = channel.toServerChannel().map(ServerChannel::getServer).orElse(null);

        ChannelCreateEventInternal fireEvent = new ChannelCreateEventInternal(discord, channel);

        collectListeners(ChannelCreateListener.class, discord, server)
                .forEach(listener -> listener.onChannelCreate(fireEvent));
    }
}
