package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.entity.message.Message;

public enum MessageEditTrait implements EditTrait<Message> {
    CONTENT,
    EMBED
}
