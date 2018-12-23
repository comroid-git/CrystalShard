package de.kaleidox.crystalshard.api.handling.event.server.role;

import de.kaleidox.crystalshard.api.handling.editevent.EditEvent;
import de.kaleidox.crystalshard.api.entity.role.Role;

public interface RoleEditEvent extends RoleEvent, EditEvent<Role> {
}
