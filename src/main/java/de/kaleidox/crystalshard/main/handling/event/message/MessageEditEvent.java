package de.kaleidox.crystalshard.main.handling.event.message;

import java.util.Optional;

public interface MessageEditEvent extends MessageCreateEvent {
    Optional<String> getPreviousContent();
}
