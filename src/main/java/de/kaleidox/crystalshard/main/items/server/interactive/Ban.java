package de.kaleidox.crystalshard.main.items.server.interactive;

import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;

public interface Ban {
    ServerMember getUser();
    
    Server getServer();
}
