package de.kaleidox.crystalshard.main.exception;

import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.util.helpers.ListHelper;

import java.util.List;

public class DiscordPermissionException extends Throwable {
    private final Permission[] lackingPermission;

    public DiscordPermissionException(String message, Permission... lackingPermission) {
        super(message);
        this.lackingPermission = lackingPermission;
    }

    public List<Permission> getLackingPermission() {
        return ListHelper.of(lackingPermission);
    }
}
