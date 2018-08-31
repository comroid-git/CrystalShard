package de.kaleidox.crystalshard.main.items.role;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.main.items.permission.PermissionApplyable;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.server.Server;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public interface Role extends DiscordItem, Nameable, Mentionable, PermissionApplyable {
    CompletableFuture<Server> getServer();

    Color getColor();

    boolean isGrouping();

    boolean isManaged();

    boolean isMentionable();

    int getPosition();

    PermissionList getPermissions();
}
