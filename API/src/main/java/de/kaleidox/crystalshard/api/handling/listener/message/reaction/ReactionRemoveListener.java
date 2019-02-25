package de.kaleidox.crystalshard.api.handling.listener.message.reaction;

import de.kaleidox.crystalshard.api.handling.event.message.reaction.ReactionRemoveEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;

@FunctionalInterface
public interface ReactionRemoveListener extends DiscordAttachableListener, ServerAttachableListener, ChannelAttachableListener, MessageAttachableListener {
    void onReactionRemove(ReactionRemoveEvent event);
}
