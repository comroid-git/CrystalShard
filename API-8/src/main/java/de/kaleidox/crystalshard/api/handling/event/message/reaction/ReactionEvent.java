package de.kaleidox.crystalshard.api.handling.event.message.reaction;

import de.kaleidox.crystalshard.api.entity.message.Reaction;
import de.kaleidox.crystalshard.api.entity.server.emoji.Emoji;
import de.kaleidox.crystalshard.api.handling.event.message.MessageEvent;
import de.kaleidox.crystalshard.api.handling.event.user.UserEvent;

public interface ReactionEvent extends MessageEvent, UserEvent {
    Reaction getReaction();

    Emoji getEmoji();

    int getCount();
}
