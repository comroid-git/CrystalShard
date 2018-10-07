package de.kaleidox.crystalshard.main.items.permission;

import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.stream.Stream;

public interface PermissionApplyable {
    boolean hasPermission(User user, Permission permission);
    
    default boolean hasPermission(Discord discord, Permission permission) {
        return hasPermission(discord.getSelf(), permission);
    }
    
    default boolean hasPermission(Discord discord, Permission... permissions) {
        return hasPermission(discord.getSelf(), permissions);
    }
    
    default boolean hasPermission(User user, Permission... permissions) {
        return Stream.of(permissions).allMatch(perm -> hasPermission(user, perm));
    }
}
