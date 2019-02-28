package de.kaleidox.crystalshard.api.handling.event.channel.other;

import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.handling.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.api.handling.event.user.UserEvent;

public interface TypingStartEvent extends ChannelEvent, UserEvent {
    default TextChannel getTextChannel() {
        return getChannel().asTextChannel()
                .orElseThrow(AssertionError::new);
    }
}
