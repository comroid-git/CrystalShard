package de.comroid.crystalshard.core.api.gateway.event.channel.pin;

// https://discordapp.com/developers/docs/topics/gateway#channel-pins-update

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.channel.pin.ChannelPinsUpdateListener;
import de.comroid.crystalshard.util.annotation.IntroducedBy;
import de.comroid.crystalshard.util.annotation.InitializedBy;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

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
