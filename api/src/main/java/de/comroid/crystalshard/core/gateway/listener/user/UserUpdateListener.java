package de.comroid.crystalshard.core.gateway.listener.user;

import de.comroid.crystalshard.core.gateway.event.UserUpdateEvent;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface UserUpdateListener extends GatewayListener<UserUpdateEvent> {
    interface Manager extends GatewayListenerManager<UserUpdateListener> {
    }
}
