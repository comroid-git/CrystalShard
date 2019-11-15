package de.comroid.crystalshard.core.gateway.event;

import de.comroid.crystalshard.api.event.EventBase;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

public interface GatewayEventBase extends EventBase, JsonDeserializable {
    default Gateway getGateway() {
        return getAPI().getGateway();
    }
}
