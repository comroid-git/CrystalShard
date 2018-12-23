package de.kaleidox.crystalshard.api.handling.event.channel.other;

import de.kaleidox.crystalshard.api.handling.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.api.handling.event.user.UserEvent;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;

public interface TypingStartEvent extends ChannelEvent, UserEvent {
    default TextChannel getTextChannel() {
        return getChannel().toTextChannel()
                .orElseThrow(AssertionError::new);
    }
}
