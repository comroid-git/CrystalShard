package de.comroid.crystalshard.core.api.gateway.listener.user;

import de.comroid.crystalshard.core.api.gateway.event.UserUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface UserUpdateListener extends GatewayListener<UserUpdateEvent> {
    interface Manager extends GatewayListenerManager<UserUpdateListener> {
    }
}
