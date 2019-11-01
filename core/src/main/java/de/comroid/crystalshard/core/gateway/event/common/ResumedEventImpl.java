package de.comroid.crystalshard.core.gateway.event.common;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.core.api.gateway.event.common.ResumedEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#resumed")
public class ResumedEventImpl extends AbstractGatewayEvent implements ResumedEvent {
    public ResumedEventImpl(Discord api, JsonNode data) {
        super(api, data);
    }
}
