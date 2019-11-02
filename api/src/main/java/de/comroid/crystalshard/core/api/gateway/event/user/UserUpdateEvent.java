package de.comroid.crystalshard.core.api.gateway.event.user;

// https://discordapp.com/developers/docs/topics/gateway#user-update

import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEvent;
import de.comroid.crystalshard.core.api.gateway.listener.user.UserUpdateListener;
import de.comroid.crystalshard.util.annotation.InitializedBy;

public interface UserUpdateEvent extends GatewayEvent {
    String NAME = "USER_UPDATE";

    User getUser();
}
