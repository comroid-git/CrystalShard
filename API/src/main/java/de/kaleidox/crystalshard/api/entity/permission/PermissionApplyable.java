package de.kaleidox.crystalshard.api.entity.permission;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.entity.user.User;

import java.util.stream.Stream;

public interface PermissionApplyable {
    default boolean hasPermission(Discord discord, Permission permission) {
        return hasPermission(discord.getSelf(), permission);
    }

    boolean hasPermission(User user, Permission permission);

    default boolean hasPermission(Discord discord, Permission... permissions) {
        return hasPermission(discord.getSelf(), permissions);
    }

    default boolean hasPermission(User user, Permission... permissions) {
        return Stream.of(permissions)
                .allMatch(perm -> hasPermission(user, perm));
    }
}
