package de.kaleidox.crystalshard.main.handling.event.message;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;

import java.util.Optional;

public interface MessageEditEvent extends MessageCreateEvent, EditEvent<Message> {
    Optional<String> getPreviousContent();

    Optional<SentEmbed> getPreviousEmbed();
}
