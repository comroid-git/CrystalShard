package de.kaleidox.crystalshard.core.api.gateway.event.webhook;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import de.kaleidox.crystalshard.api.entity.channel.GuildChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface WebhooksUpdateEvent extends GatewayEvent {
    String NAME = "WEBHOOKS_UPDATE";

    Guild getGuild();

    GuildChannel getChannel();
}
