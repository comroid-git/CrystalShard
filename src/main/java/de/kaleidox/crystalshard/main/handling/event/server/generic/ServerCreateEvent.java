package de.kaleidox.crystalshard.main.handling.event.server.generic;

import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public interface ServerCreateEvent extends ServerEvent {
    ServerMember getOwner();
}
