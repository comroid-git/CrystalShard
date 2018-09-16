package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.permission.PermissionOverwritable;
import de.kaleidox.crystalshard.main.items.server.Server;

public interface ServerMember extends User, PermissionOverwritable {
    Server getServer();
}
