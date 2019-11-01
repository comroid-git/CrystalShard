package de.comroid.crystalshard.core.gateway.event.common;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.common.HelloEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#hello")
public class HelloEventImpl extends AbstractGatewayEvent implements HelloEvent {
    protected @JsonData("heartbeat_interval") int heartbeatInterval;

    public HelloEventImpl(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }
}
