package de.kaleidox.crystalshard.api.handling.event.server.ban;

import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.interactive.Ban;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;

public interface ServerUnbanEvent extends ServerEvent {
    // Override Methods
    default Server getServer() {
        return getUnban().getServer();
    }

    Ban getUnban();

    default ServerMember getUnbannedUser() {
        return getUnban().getUser();
    }
}
