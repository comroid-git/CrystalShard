package de.kaleidox.crystalshard.main.handling.editevent.enums;

import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;

public enum ServerEmojiEditTrait implements EditTrait<CustomEmoji> {
    CREATION,

    NAME,

    WHILTELISTED_ROLES,

    DELETION
}
