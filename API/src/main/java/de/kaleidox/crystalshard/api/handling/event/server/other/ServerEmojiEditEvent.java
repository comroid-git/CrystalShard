package de.kaleidox.crystalshard.api.handling.event.server.other;

import de.kaleidox.crystalshard.api.entity.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;

import java.util.List;

public interface ServerEmojiEditEvent extends ServerEvent, EditEvent<CustomEmoji> {
    List<CustomEmoji> getAddedEmojis();

    List<CustomEmoji> getEditedEmojis();

    List<CustomEmoji> getDeletedEmojis();
}
