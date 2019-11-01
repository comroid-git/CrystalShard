package de.comroid.crystalshard.core.api.gateway.event;

import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.api.model.ApiBound;
import de.comroid.crystalshard.core.api.gateway.Gateway;

public interface GatewayEvent extends Event, ApiBound {
    String NAME = null;

    default Gateway getGateway() {
        return getAPI().getGateway();
    }
}
