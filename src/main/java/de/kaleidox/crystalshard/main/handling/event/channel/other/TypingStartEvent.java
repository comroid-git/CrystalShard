package de.kaleidox.crystalshard.main.handling.event.channel.other;

import de.kaleidox.crystalshard.main.handling.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.main.handling.event.user.UserEvent;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;

public interface TypingStartEvent extends ChannelEvent, UserEvent {
    default TextChannel getTextChannel() {
        return getChannel().toTextChannel().orElseThrow(AssertionError::new);
    }
}
