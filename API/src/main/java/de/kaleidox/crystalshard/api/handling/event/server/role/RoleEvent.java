package de.kaleidox.crystalshard.api.handling.event.server.role;

import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;

public interface RoleEvent extends ServerEvent {
    default long getRoleId() {
        return getRole().getId();
    }

    Role getRole();
}
