package de.kaleidox.crystalshard.api.handling.listener.user;

import de.kaleidox.crystalshard.api.handling.event.user.UserUpdateEvent;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;

@FunctionalInterface
public interface UserUpdateListener extends DiscordAttachableListener, UserAttachableListener {
    void onUserUpdate(UserUpdateEvent event);
}
