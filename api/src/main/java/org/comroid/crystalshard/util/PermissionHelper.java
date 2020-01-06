package org.comroid.crystalshard.util;

import org.comroid.crystalshard.api.entity.channel.GuildChannel;
import org.comroid.crystalshard.api.model.permission.Permission;
import org.comroid.crystalshard.api.model.permission.PermissionOverridable;

public final class PermissionHelper {
    private PermissionHelper() {
        // nope
    }

    public static boolean checkPermission(Permission permission, PermissionOverridable target, GuildChannel context) {
        return false; // todo
    }
}
