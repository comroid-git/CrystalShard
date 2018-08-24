package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.permission.PermissionApplyable;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.util.Optional;

public interface ServerChannel extends Channel, Nameable, PermissionApplyable {
    Server getServer();

    Optional<ChannelCategory> getCategory();
}
