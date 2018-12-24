package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.handling.listener.server.other.ServerWebhookUpdateListener;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.other.ServerWebhookUpdateEventInternal;

/**
 * guild_id	    snowflake	id of the guild channel_id	snowflake	id of the channel
 */
public class WEBHOOKS_UPDATE extends HandlerBase {
    // Override Methods
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        long serverId = data.get("guild_id")
                .asLong();
        long channelId = data.get("channel_id")
                .asLong();
        Server server = discord.getServerCache()
                .getOrRequest(serverId, serverId);
        Channel channel = discord.getChannelCache()
                .getOrRequest(channelId, channelId);

        ServerWebhookUpdateEventInternal event = new ServerWebhookUpdateEventInternal(discord, server, channel);

        collectListeners(ServerWebhookUpdateListener.class, discord, server, channel).forEach(listener -> discord.getThreadPool()
                .execute(() -> listener.onWebhookUpdate(event)));
    }
}
