package de.kaleidox.crystalshard.core.api.gateway.event;

import de.kaleidox.crystalshard.api.event.model.Event;
import de.kaleidox.crystalshard.api.model.ApiBound;
import de.kaleidox.crystalshard.core.api.gateway.Gateway;

public interface GatewayEvent extends Event, ApiBound {
    String NAME = null;

    default Gateway getGateway() {
        return getAPI().getGateway();
    }
}
