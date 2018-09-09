package de.kaleidox.crystalshard.main.handling.event.server.other;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;

public interface ServerEmojiEditEvent extends ServerEvent, EditEvent<CustomEmoji> {
    CustomEmoji getEditedEmoji();
}
