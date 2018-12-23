package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.entity.server.emoji.CustomEmoji;

public enum ServerEmojiEditTrait implements EditTrait<CustomEmoji> {
    CREATION,
    NAME,
    WHILTELISTED_ROLES,
    DELETION
}
