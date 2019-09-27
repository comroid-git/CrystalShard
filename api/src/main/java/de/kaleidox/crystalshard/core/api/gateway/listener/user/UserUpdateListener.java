package de.kaleidox.crystalshard.core.api.gateway.listener.user;

import de.kaleidox.crystalshard.core.api.gateway.event.user.UserUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface UserUpdateListener extends GatewayListener<UserUpdateEvent> {
    interface Manager extends GatewayListenerManager<UserUpdateListener> {
    }
}
