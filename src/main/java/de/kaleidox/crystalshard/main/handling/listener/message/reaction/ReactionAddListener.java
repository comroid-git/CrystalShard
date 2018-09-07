package de.kaleidox.crystalshard.main.handling.listener.message.reaction;

import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionAddEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;

@FunctionalInterface
public interface ReactionAddListener extends MessageAttachableListener {
    void onEvent(ReactionAddEvent event);
}
