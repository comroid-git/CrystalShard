package de.kaleidox.crystalshard.core.api.gateway.event.user;

// https://discordapp.com/developers/docs/topics/gateway#user-update

import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface UserUpdateEvent extends GatewayEvent {
    String NAME = "USER_UPDATE";

    User getUser();
}
