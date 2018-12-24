package de.kaleidox.crystalshard.api.handling.event.server.ban;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.server.interactive.Ban;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;
import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;

public interface ServerBanEvent extends ServerEvent {
    // Override Methods
    default Server getServer() {
        return getBan().getServer();
    }

    Ban getBan();

    default ServerMember getBannedUser() {
        return getBan().getUser();
    }
}
