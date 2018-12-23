package de.kaleidox.crystalshard.api.handling.event.server.role;

import de.kaleidox.crystalshard.api.handling.event.server.ServerEvent;
import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.role.Role;

import java.util.Optional;

public interface RoleDeleteEvent extends ServerEvent {
    default Optional<Long> getRoleId() {
        return getRole().map(DiscordItem::getId);
    }

    Optional<Role> getRole();
}
