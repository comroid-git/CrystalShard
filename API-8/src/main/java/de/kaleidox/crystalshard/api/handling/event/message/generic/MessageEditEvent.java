package de.kaleidox.crystalshard.api.handling.event.message.generic;

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.message.embed.SentEmbed;
import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;

import java.util.Optional;

public interface MessageEditEvent extends MessageCreateEvent, EditEvent<Message> {
    Optional<String> getPreviousContent();

    Optional<SentEmbed> getPreviousEmbed();
}
