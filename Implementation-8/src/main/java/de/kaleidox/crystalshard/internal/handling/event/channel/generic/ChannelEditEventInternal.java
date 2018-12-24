package de.kaleidox.crystalshard.internal.handling.event.channel.generic;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.handling.event.channel.generic.ChannelEditEvent;
import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;

import java.util.Set;

public class ChannelEditEventInternal extends EventBase implements ChannelEditEvent {
    private final Channel channel;
    private final Set<EditTrait<Channel>> traits;

    public ChannelEditEventInternal(DiscordInternal discordInternal, Channel channel, Set<EditTrait<Channel>> traits) {
        super(discordInternal);
        this.channel = channel;
        this.traits = traits;
    }

    // Override Methods
    @Override
    public Set<EditTrait<Channel>> getEditTraits() {
        return traits;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
