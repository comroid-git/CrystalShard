package de.comroid.crystalshard.util;

import de.comroid.crystalshard.api.entity.channel.GuildChannel;
import de.comroid.crystalshard.api.model.permission.Permission;
import de.comroid.crystalshard.api.model.permission.PermissionOverridable;

public final class PermissionHelper {
    private PermissionHelper() {
        // nope
    }

    public static boolean checkPermission(Permission permission, PermissionOverridable target, GuildChannel context) {
        return false; // todo
    }
}
