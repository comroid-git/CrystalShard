package de.kaleidox.crystalshard.core.api.gateway.listener.user.presence;

import de.kaleidox.crystalshard.core.api.gateway.event.user.presence.PresenceUpdateEvent;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;

@FunctionalInterface
public interface PresenceUpdateListener extends GatewayListener<PresenceUpdateEvent> {
    void onPresenceUpdate(PresenceUpdateEvent event);
}
