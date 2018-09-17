package de.kaleidox.crystalshard.internal.handling.event.channel.generic;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.channel.generic.ChannelDeleteEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;

import java.util.Optional;

public class ChannelDeleteEventInternal extends EventBase implements ChannelDeleteEvent {
    private final Channel channel;

    public ChannelDeleteEventInternal(DiscordInternal discordInternal, Channel channel) {
        super(discordInternal);
        this.channel = channel;
    }

// Override Methods
    @Override
    public Optional<Channel> getChannel() {
        return Optional.ofNullable(channel);
    }
}
