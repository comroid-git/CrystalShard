package de.kaleidox.crystalshard.api.exception;

import de.kaleidox.crystalshard.api.entity.server.permission.Permission;
import de.kaleidox.util.helpers.ListHelper;

import java.util.List;

public class DiscordPermissionException extends RuntimeException {
    private final Permission[] lackingPermission;

    public DiscordPermissionException(String message, Permission... lackingPermission) {
        super(message);
        this.lackingPermission = lackingPermission;
    }

    public List<Permission> getLackingPermission() {
        return ListHelper.of(lackingPermission);
    }
}
