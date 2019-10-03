package de.kaleidox.crystalshard.core.gateway.event.common;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.common.InvalidSessionEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#invalid-session")
public class InvalidSessionEventImpl extends AbstractGatewayEvent implements InvalidSessionEvent {
    protected @JsonData boolean resumable;

    public InvalidSessionEventImpl(Discord api, JsonNode data) {
        super(api, data);
    }

    @Override
    public boolean isResumable() {
        return resumable;
    }
}
