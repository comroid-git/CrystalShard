package de.kaleidox.crystalshard.main.items.role;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.permission.PermissionApplyable;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;

public interface Role extends DiscordItem, MessageReciever, PermissionApplyable {
}
