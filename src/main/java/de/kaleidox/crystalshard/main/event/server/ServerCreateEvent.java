package de.kaleidox.crystalshard.main.event.server;

import de.kaleidox.crystalshard.main.items.user.ServerMember;

public interface ServerCreateEvent extends ServerEvent {
    ServerMember getOwner();
}
