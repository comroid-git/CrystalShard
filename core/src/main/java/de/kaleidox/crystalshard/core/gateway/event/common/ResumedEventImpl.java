package de.kaleidox.crystalshard.core.gateway.event.common;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.listener.model.ListenerAttachable;
import de.kaleidox.crystalshard.core.api.gateway.event.common.ResumedEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#resumed")
public class ResumedEventImpl extends AbstractGatewayEvent implements ResumedEvent {
    public ResumedEventImpl(Discord api, JsonNode data) {
        super(api, data);
    }
}
