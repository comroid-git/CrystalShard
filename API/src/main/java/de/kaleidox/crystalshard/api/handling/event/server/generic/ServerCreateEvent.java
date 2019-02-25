package de.kaleidox.crystalshard.api.handling.event.server.generic;

import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;

public interface ServerCreateEvent extends ServerEvent {
    ServerMember getOwner();
}
