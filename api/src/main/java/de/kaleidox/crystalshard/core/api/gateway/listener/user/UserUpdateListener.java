package de.kaleidox.crystalshard.core.api.gateway.listener.user;

import de.kaleidox.crystalshard.core.api.gateway.event.user.UserUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface UserUpdateListener extends GatewayListener<UserUpdateEvent> {
    void onUserUpdate(UserUpdateEvent event);
}
