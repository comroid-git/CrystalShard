package de.comroid.crystalshard.core.gateway.event;

import java.util.Set;
import java.util.concurrent.Semaphore;

import de.comroid.crystalshard.api.event.EventBase;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

public interface GatewayEventBase extends EventBase, JsonDeserializable {
    default Gateway getGateway() {
        return getAPI().getGateway();
    }

    @SuppressWarnings("rawtypes")
    default Set<JSONBinding> changed() {
        return initialBindings();
    }
}
