package de.kaleidox.crystalshard.main.items.permission;

import de.kaleidox.crystalshard.main.items.DiscordItem;

public interface PermissionApplyable extends DiscordItem {
    PermissionList getListFor(DiscordItem scope);
}
