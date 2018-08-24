package de.kaleidox.crystalshard.main.items.permission;

import de.kaleidox.crystalshard.internal.items.permission.PermissionOverrideInternal;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Map;

public interface PermissionOverride extends Map<Permission, OverrideState> {
    static PermissionOverride ofParent(PermissionApplyable parent, User scope) {
        return new PermissionOverrideInternal(parent, scope);
    }

    PermissionApplyable getParent();

    PermissionOverride addOverride(Permission permission, OverrideState state);

    PermissionOverride removeOverride(Permission permission);
}

