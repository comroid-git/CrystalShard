package de.kaleidox.crystalshard.main.items.channel;

import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.permission.PermissionOverride;
import de.kaleidox.crystalshard.main.items.server.Server;
import java.util.List;
import java.util.Optional;

public interface ServerChannel extends Channel, Nameable {
    Server getServer();
    
    Optional<ChannelCategory> getCategory();
    
    List<PermissionOverride> getPermissionOverrides();
}
