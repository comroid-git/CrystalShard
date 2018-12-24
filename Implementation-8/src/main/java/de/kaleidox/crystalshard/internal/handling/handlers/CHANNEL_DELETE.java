package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.ServerChannel;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.listener.channel.generic.ChannelDeleteListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.channel.generic.ChannelDeleteEventInternal;

/**
 * https://discordapp.com/developers/docs/topics/gateway#channel-delete
 */
public class CHANNEL_DELETE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Channel channel = discord.getChannelCache()
                .getOrCreate(discord, data);
        Server server = channel.toServerChannel()
                .map(ServerChannel::getServer)
                .orElse(null);

        ChannelDeleteEventInternal event = new ChannelDeleteEventInternal(discord, channel);

        // todo Mark old Channel as deletable

        collectListeners(ChannelDeleteListener.class, discord, server, channel).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onChannelDelete(event)));

        channel.detachAllListeners();
    }
}
