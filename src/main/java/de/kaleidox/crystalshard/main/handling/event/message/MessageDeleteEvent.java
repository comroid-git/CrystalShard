package de.kaleidox.crystalshard.main.handling.event.message;

import de.kaleidox.crystalshard.main.handling.event.channel.ChannelEvent;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Optional;

public interface MessageDeleteEvent extends MessageEvent, ChannelEvent {
    Optional<User> getDeleter();
}
