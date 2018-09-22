package de.kaleidox.crystalshard.main.items.permission;

import de.kaleidox.crystalshard.main.items.user.User;

public interface PermissionApplyable {
    boolean hasPermission(User user, Permission permission);
}
