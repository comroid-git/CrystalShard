package de.kaleidox.crystalshard.core.gateway.event.channel.pin;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.channel.pin.ChannelPinsUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#channel-pins-update")
public class ChannelPinsUpdateEventImpl extends AbstractGatewayEvent implements ChannelPinsUpdateEvent {
    protected @JsonData("guild_id") long guildId;
    protected @JsonData("channel_id") long channelId;
    protected @JsonData("last_pin_timestamp") @Nullable Instant lastPinTimestamp;

    private @Nullable Guild guild;
    private TextChannel channel;

    public ChannelPinsUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        guild = api.getCacheManager()
                .getGuildByID(guildId)
                .orElse(null);
        channel = api.getCacheManager()
                .getChannelByID(channelId)
                .flatMap(Channel::asTextChannel)
                .orElseThrow(() -> new AssertionError("No valid Channel ID was sent with this ChannelPinsUpdateEvent!"));

        // todo request changed pin here?

        getGuild().ifPresent(this::affects);
        affects(channel);
    }

    @Override
    public Optional<Guild> getGuild() {
        return Optional.ofNullable(guild);
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public Optional<Instant> getLastPinTimestamp() {
        return Optional.ofNullable(lastPinTimestamp);
    }
}
