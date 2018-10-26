package de.kaleidox.crystalshard.main.items.permission;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.server.Server;

public interface PermissionOverwritable extends DiscordItem {
    Server getServer();
}
