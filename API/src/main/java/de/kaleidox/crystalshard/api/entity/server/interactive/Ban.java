package de.kaleidox.crystalshard.api.entity.server.interactive;

import de.kaleidox.crystalshard.api.entity.server.Server;
import de.kaleidox.crystalshard.api.entity.user.ServerMember;

public interface Ban {
    ServerMember getUser();

    Server getServer();
}
