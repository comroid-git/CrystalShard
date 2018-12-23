package de.kaleidox.crystalshard.api.handling.event.server.generic;

import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;

public interface ServerCreateEvent extends ServerEvent {
    ServerMember getOwner();
}
