package de.kaleidox.crystalshard.core.gateway.event.common;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.common.HelloEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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
