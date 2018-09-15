package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.permission.PermissionOverwritable;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.util.UserContainer;

public interface ServerMember extends User, PermissionOverwritable {
    Server getServer();
}
