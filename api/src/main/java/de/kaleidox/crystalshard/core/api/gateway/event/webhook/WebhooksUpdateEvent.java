package de.kaleidox.crystalshard.core.api.gateway.event.webhook;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import de.kaleidox.crystalshard.api.entity.channel.GuildTextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.webhook.WebhooksUpdateListener;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

@ManagedBy(WebhooksUpdateListener.Manager.class)
public interface WebhooksUpdateEvent extends GatewayEvent {
    String NAME = "WEBHOOKS_UPDATE";

    Guild getGuild();

    GuildTextChannel getChannel();
}
