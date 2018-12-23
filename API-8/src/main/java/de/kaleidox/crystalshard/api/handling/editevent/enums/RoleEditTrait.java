package de.kaleidox.crystalshard.api.handling.editevent.enums;

import de.kaleidox.crystalshard.api.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.api.entity.role.Role;

public enum RoleEditTrait implements EditTrait<Role> {
    GROUPING,
    MENTIONABILITY,
    NAME,
    COLOR,
    POSITION,
    MANAGED,
    PERMISSION_OVERWRITES
}
