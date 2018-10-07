package de.kaleidox.crystalshard.main.handling.event.server.ban;

import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.interactive.Ban;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public interface ServerBanEvent extends ServerEvent {
    Ban getBan();
    
    // Override Methods
    default Server getServer() {
        return getBan().getServer();
    }
    
    default ServerMember getBannedUser() {
        return getBan().getUser();
    }
}
