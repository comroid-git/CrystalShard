package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;

public enum MessageEditTrait implements EditTrait<Message> {
    CONTENT,
    EMBED
}
