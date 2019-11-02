package de.comroid.crystalshard.core.api.gateway.event.webhook;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.webhook.WebhooksUpdateListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface WebhooksUpdateEvent extends GatewayEvent {
    String NAME = "WEBHOOKS_UPDATE";

    Guild getGuild();

    GuildTextChannel getChannel();
}
