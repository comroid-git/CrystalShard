package de.kaleidox.crystalshard.main.handling.event.server.role;

import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.role.Role;

public interface RoleEvent extends ServerEvent {
    Role getRole();
    
    default long getRoleId() {
        return getRole().getId();
    }
}
