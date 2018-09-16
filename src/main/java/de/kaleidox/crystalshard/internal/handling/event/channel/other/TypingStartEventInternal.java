package de.kaleidox.crystalshard.internal.handling.event.channel.other;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.internal.handling.event.EventBase;
import de.kaleidox.crystalshard.main.handling.event.channel.other.TypingStartEvent;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.user.User;

public class TypingStartEventInternal extends EventBase implements TypingStartEvent {
    private final Channel channel;
    private final User user;

    public TypingStartEventInternal(DiscordInternal discordInternal,
                                    Channel channel,
                                    User user) {
        super(discordInternal);
        this.channel = channel;
        this.user = user;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public User getUser() {
        return user;
    }
}
