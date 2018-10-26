package de.kaleidox.crystalshard.main.handling.listener.user;

import de.kaleidox.crystalshard.main.handling.event.user.UserUpdateEvent;
import de.kaleidox.crystalshard.main.handling.listener.DiscordAttachableListener;

@FunctionalInterface
public interface UserUpdateListener extends DiscordAttachableListener, UserAttachableListener {
    void onUserUpdate(UserUpdateEvent event);
}
