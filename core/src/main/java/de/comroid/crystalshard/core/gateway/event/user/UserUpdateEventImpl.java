package de.comroid.crystalshard.core.gateway.event.user;

import de.comroid.crystalshard.abstraction.gateway.AbstractGatewayEvent;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.annotation.JsonData;
import de.comroid.crystalshard.core.api.gateway.event.user.UserUpdateEvent;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import com.fasterxml.jackson.databind.JsonNode;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;

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
