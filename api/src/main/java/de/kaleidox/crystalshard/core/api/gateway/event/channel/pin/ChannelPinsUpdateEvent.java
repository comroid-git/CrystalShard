package de.kaleidox.crystalshard.core.api.gateway.event.channel.pin;

// https://discordapp.com/developers/docs/topics/gateway#channel-pins-update

import java.time.Instant;
import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface ChannelPinsUpdateEvent extends GatewayEvent {
    String NAME = "CHANNEL_PINS_UPDATE";

    Optional<Guild> getGuild();

    TextChannel getChannel();

    Optional<Instant> getLastPinTimestamp();
}
