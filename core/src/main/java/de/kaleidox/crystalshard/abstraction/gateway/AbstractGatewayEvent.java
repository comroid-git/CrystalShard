package de.kaleidox.crystalshard.abstraction.gateway;

import de.kaleidox.crystalshard.abstraction.event.AbstractEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.api.gateway.Gateway;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractGatewayEvent extends AbstractEvent implements GatewayEvent {
    protected final Gateway gateway;

    protected AbstractGatewayEvent(Discord api, JsonNode data) {
        super(api, data);

        gateway = api.getGateway();
    }
}
