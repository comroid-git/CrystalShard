package de.kaleidox.crystalshard.core.api.gateway.listener.user.presence;

import de.kaleidox.crystalshard.core.api.gateway.event.user.presence.PresenceUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;

public interface PresenceUpdateListener extends GatewayListener<PresenceUpdateEvent> {
    interface Manager extends GatewayListenerManager<PresenceUpdateListener> {
    }
}
