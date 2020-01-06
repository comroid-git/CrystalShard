package org.comroid.crystalshard.api.exception;

import java.util.Arrays;

import org.comroid.crystalshard.api.model.permission.Permission;

public class DiscordPermissionException extends RuntimeException {
    public final Permission[] permissions;

    public DiscordPermissionException(Permission... permissions) {
        super("Missing permission" + (permissions.length == 1 ? "" : "s") + ": " + Arrays.toString(permissions));
        this.permissions = permissions;
    }
}
