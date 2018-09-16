package de.kaleidox.crystalshard.main.handling.editevent.enums;

import de.kaleidox.crystalshard.main.handling.editevent.EditTrait;
import de.kaleidox.crystalshard.main.items.role.Role;

public enum RoleEditTrait implements EditTrait<Role> {
    GROUPING,

    MENTIONABILITY,

    NAME, COLOR, POSITION, MANAGED, PERMISSION_OVERWRITES
}
