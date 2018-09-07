package de.kaleidox.crystalshard.main.event.message.reaction;

import de.kaleidox.crystalshard.main.event.UserEvent;
import de.kaleidox.crystalshard.main.event.message.MessageEvent;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;

public interface ReactionEvent extends MessageEvent, UserEvent {
    Reaction getReaction();

    Emoji getEmoji();

    int newReactionCount();
}
