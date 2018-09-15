package de.kaleidox.crystalshard.main.items.server.interactive;

import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;

public interface Ban {
    ServerMember getUser();

    Server getServer();
}
