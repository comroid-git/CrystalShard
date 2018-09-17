package de.kaleidox.crystalshard.internal.handling.event.channel.generic;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.channel.generic.ChannelCreateEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;

public class ChannelCreateEventInternal extends EventBase implements ChannelCreateEvent {
    private final Channel createdChannel;

    public ChannelCreateEventInternal(DiscordInternal discordInternal, Channel createdChannel) {
        super(discordInternal);
        this.createdChannel = createdChannel;
    }

// Override Methods
    @Override
    public Channel getChannel() {
        return createdChannel;
    }
}
