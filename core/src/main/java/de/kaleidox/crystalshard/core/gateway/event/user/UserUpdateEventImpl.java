package de.kaleidox.crystalshard.core.gateway.event.user;

import java.lang.annotation.Inherited;

import de.kaleidox.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.annotation.JsonData;
import de.kaleidox.crystalshard.core.api.gateway.event.user.UserUpdateEvent;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

@IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/topics/gateway#user-update")
public class UserUpdateEventImpl extends AbstractGatewayEvent implements UserUpdateEvent {
    protected @JsonData User user;

    public UserUpdateEventImpl(Discord api, JsonNode data) {
        super(api, data);

        affects(user);
    }

    @Override
    public User getUser() {
        return user;
    }
}
