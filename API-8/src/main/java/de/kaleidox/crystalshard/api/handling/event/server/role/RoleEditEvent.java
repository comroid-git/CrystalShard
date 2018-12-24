package de.kaleidox.crystalshard.api.handling.event.server.role;

import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;

public interface RoleEditEvent extends RoleEvent, EditEvent<Role> {
}
