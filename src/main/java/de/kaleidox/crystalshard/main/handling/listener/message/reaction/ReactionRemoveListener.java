package de.kaleidox.crystalshard.main.handling.listener.message.reaction;

import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionRemoveEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;

@FunctionalInterface
public interface ReactionRemoveListener extends MessageAttachableListener {
    void onReactionRemove(ReactionRemoveEvent event);
}
