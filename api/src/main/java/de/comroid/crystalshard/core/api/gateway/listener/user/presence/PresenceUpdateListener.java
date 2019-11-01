package de.comroid.crystalshard.core.api.gateway.listener.user.presence;

import de.comroid.crystalshard.core.api.gateway.event.user.presence.PresenceUpdateEvent;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface PresenceUpdateListener extends GatewayListener<PresenceUpdateEvent> {
    interface Manager extends GatewayListenerManager<PresenceUpdateListener> {
    }
}
