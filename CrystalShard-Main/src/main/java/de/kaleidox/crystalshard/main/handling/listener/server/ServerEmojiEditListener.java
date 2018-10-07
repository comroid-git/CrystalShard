package de.kaleidox.crystalshard.main.handling.listener.server;

import de.kaleidox.crystalshard.main.handling.event.server.other.ServerEmojiEditEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;

@FunctionalInterface
public interface ServerEmojiEditListener extends DiscordAttachableListener, ServerAttachableListener {
    void onEmojiEdit(ServerEmojiEditEvent event);
}
