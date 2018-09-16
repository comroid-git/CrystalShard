package de.kaleidox.crystalshard.internal.handling.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.server.other.ServerWebhookUpdateEventInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.main.handling.listener.server.other.ServerWebhookUpdateListener;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.server.Server;

/**
 * guild_id	    snowflake	id of the guild
 * channel_id	snowflake	id of the channel
 */
public class WEBHOOKS_UPDATE extends HandlerBase {
    @Override
    public void handle(DiscordInternal discord, JsonNode data) {
        Server server = ServerInternal.getInstance(discord, data.get("guild_id").asLong());
        Channel channel = ChannelInternal.getInstance(discord, data.get("channel_id").asLong());

        ServerWebhookUpdateEventInternal event = new ServerWebhookUpdateEventInternal(discord, server, channel);

        collectListeners(ServerWebhookUpdateListener.class, discord, server, channel)
                .forEach(listener -> discord.getThreadPool()
                        .execute(() -> listener.onWebhookUpdate(event))
                );
    }
}
