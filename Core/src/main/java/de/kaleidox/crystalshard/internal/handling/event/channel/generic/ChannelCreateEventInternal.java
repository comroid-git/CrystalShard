package de.kaleidox.crystalshard.internal.handling.event.channel.generic;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.handling.event.channel.generic.ChannelCreateEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

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
