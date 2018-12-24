package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.entity.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;

public enum ServerEmojiEditTrait implements EditTrait<CustomEmoji> {
    CREATION,
    NAME,
    WHILTELISTED_ROLES,
    DELETION
}
