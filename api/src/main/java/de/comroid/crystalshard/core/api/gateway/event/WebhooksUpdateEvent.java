package de.comroid.crystalshard.core.api.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#voice-state-update

import de.comroid.crystalshard.api.entity.channel.GuildTextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;

public interface WebhooksUpdateEvent extends GatewayEventBase {
    String NAME = "WEBHOOKS_UPDATE";

    Guild getGuild();

    GuildTextChannel getChannel();
}
