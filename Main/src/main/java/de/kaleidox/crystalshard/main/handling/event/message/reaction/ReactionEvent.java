package de.kaleidox.crystalshard.main.handling.event.message.reaction;

import de.kaleidox.crystalshard.main.handling.event.message.MessageEvent;
import de.kaleidox.crystalshard.main.handling.event.user.UserEvent;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;

public interface ReactionEvent extends MessageEvent, UserEvent {
    Reaction getReaction();

    Emoji getEmoji();

    int getCount();
}
