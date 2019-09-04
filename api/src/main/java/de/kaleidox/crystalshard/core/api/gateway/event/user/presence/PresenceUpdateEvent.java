package de.kaleidox.crystalshard.core.api.gateway.event.user.presence;

// https://discordapp.com/developers/docs/topics/gateway#presence-update

import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.model.user.Presence;
import de.kaleidox.crystalshard.core.api.gateway.event.GatewayEvent;

public interface PresenceUpdateEvent extends GatewayEvent {
    String NAME = "PRESENCE_UPDATE_EVENT";

    User getUser();

    Presence getPresence();
}
