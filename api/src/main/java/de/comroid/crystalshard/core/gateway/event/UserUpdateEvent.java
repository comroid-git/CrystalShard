package de.comroid.crystalshard.core.gateway.event;

// https://discordapp.com/developers/docs/topics/gateway#user-update

import de.comroid.crystalshard.api.entity.user.User;

public interface UserUpdateEvent extends GatewayEventBase {
    String NAME = "USER_UPDATE";

    User getUser();
}
