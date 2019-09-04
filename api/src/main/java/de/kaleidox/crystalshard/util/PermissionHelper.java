package de.kaleidox.crystalshard.util;

import de.kaleidox.crystalshard.api.entity.channel.GuildChannel;
import de.kaleidox.crystalshard.api.model.permission.Permission;
import de.kaleidox.crystalshard.api.model.permission.PermissionOverridable;

public final class PermissionHelper {
    private PermissionHelper() {
        // nope
    }

    public static boolean checkPermission(Permission permission, PermissionOverridable target, GuildChannel context) {
        return false; // todo
    }
}
