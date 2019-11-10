package de.comroid.crystalshard.core.gateway.listener.user;

import de.comroid.crystalshard.core.gateway.event.USER_UPDATE;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;

public interface UserUpdateListener extends GatewayListener<USER_UPDATE> {
    interface Manager extends GatewayListenerManager<UserUpdateListener> {
    }
}
