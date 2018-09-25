package de.kaleidox.crystalshard.main.items.user;

import de.kaleidox.crystalshard.main.items.permission.PermissionOverwritable;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ServerMember extends User, PermissionOverwritable {
    Server getServer();
    
    List<Role> getRoles();
    
    Optional<String> getNickname();
    
    boolean isMuted();
    
    boolean isDeafened();
    
    Instant getJoinedInstant();
}
