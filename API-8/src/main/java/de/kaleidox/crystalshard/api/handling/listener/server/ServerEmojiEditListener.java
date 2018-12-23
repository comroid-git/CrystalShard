package de.kaleidox.crystalshard.api.handling.listener.server;

import de.kaleidox.crystalshard.api.handling.event.server.other.ServerEmojiEditEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;

@FunctionalInterface
public interface ServerEmojiEditListener extends DiscordAttachableListener, ServerAttachableListener {
    void onEmojiEdit(ServerEmojiEditEvent event);
}
