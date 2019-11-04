package de.comroid.crystalshard.core.api.gateway.event;

import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.api.model.ApiBound;
import de.comroid.crystalshard.core.api.gateway.Gateway;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

public interface GatewayEventBase extends Event, JsonDeserializable {
    String NAME = null;

    default Gateway getGateway() {
        return getAPI().getGateway();
    }
}
