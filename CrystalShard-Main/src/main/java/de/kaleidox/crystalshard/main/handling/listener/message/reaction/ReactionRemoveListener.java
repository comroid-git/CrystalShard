package de.kaleidox.crystalshard.main.handling.listener.message.reaction;

import de.kaleidox.crystalshard.main.handling.event.message.reaction.ReactionRemoveEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ReactionRemoveListener extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, MessageAttachableListener {
    void onReactionRemove(ReactionRemoveEvent event);
}
