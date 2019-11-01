package de.comroid.crystalshard.api.exception;

import java.util.Arrays;

import de.comroid.crystalshard.api.model.permission.Permission;

public class DiscordPermissionException extends RuntimeException {
    public final Permission[] permissions;

    public DiscordPermissionException(Permission... permissions) {
        super("Missing permission" + (permissions.length == 1 ? "" : "s") + ": " + Arrays.toString(permissions));
        this.permissions = permissions;
    }
}
