package de.kaleidox.crystalshard.main.handling.event.server.other;

import de.kaleidox.crystalshard.main.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;

import java.util.Collection;
import java.util.List;

public interface ServerEmojiEditEvent extends ServerEvent, EditEvent<CustomEmoji> {
    List<CustomEmoji> getAddedEmojis();

    List<CustomEmoji> getEditedEmojis();

    List<CustomEmoji> getDeletedEmojis();
}
