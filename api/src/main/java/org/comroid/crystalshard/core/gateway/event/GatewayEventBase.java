package org.comroid.crystalshard.core.gateway.event;

import java.util.Set;
import java.util.concurrent.Semaphore;

import org.comroid.crystalshard.api.event.EventBase;
import org.comroid.crystalshard.core.gateway.Gateway;
import org.comroid.crystalshard.util.model.serialization.JSONBinding;
import org.comroid.crystalshard.util.model.serialization.JsonDeserializable;

public interface GatewayEventBase extends EventBase, JsonDeserializable {
    default Gateway getGateway() {
        return getAPI().getGateway();
    }

    @SuppressWarnings("rawtypes")
    default Set<JSONBinding> changed() {
        return initialBindings();
    }
}
