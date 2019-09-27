package de.kaleidox.crystalshard.core.api.gateway.event.channel.pin;

// https://discordapp.com/developers/docs/topics/gateway#channel-pins-update

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.channel.pin.ChannelPinsUpdateListener;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.annotation.ManagedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

@ManagedBy(ChannelPinsUpdateListener.Manager.class)
public interface ChannelPinsUpdateEvent extends GatewayEvent {
    String NAME = "CHANNEL_PINS_UPDATE";

    Optional<Guild> getGuild();

    TextChannel getChannel();

    Optional<Instant> getLastPinTimestamp();

    @IntroducedBy(PRODUCTION)
    default CompletableFuture<Collection<Message>> requestPinnedMessages() {
        return getChannel().requestPinnedMessages();
    }
}
