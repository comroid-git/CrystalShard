package de.comroid.crystalshard.abstraction.gateway;

import de.comroid.crystalshard.abstraction.event.AbstractEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.core.api.gateway.Gateway;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractGatewayEvent extends AbstractEvent implements GatewayEvent {
    protected final Gateway gateway;

    protected AbstractGatewayEvent(Discord api, JsonNode data) {
        super(api, data);

        gateway = api.getGateway();
    }
}
