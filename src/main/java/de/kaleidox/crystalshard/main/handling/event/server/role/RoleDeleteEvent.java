package de.kaleidox.crystalshard.main.handling.event.server.role;

import de.kaleidox.crystalshard.main.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.role.Role;

import java.util.Optional;

public interface RoleDeleteEvent extends ServerEvent {
    Optional<Role> getRole();

    default Optional<Long> getRoleId() {
        return getRole().map(DiscordItem::getId);
    }
}
