package de.comroid.crystalshard.core.api.gateway.listener.user.presence;

import de.comroid.crystalshard.core.api.gateway.event.PRESENCE_UPDATE_EVENT;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface PresenceUpdateListener extends GatewayListener<PRESENCE_UPDATE_EVENT> {
    interface Manager extends GatewayListenerManager<PresenceUpdateListener> {
    }
}
