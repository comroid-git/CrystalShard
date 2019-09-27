package de.kaleidox.crystalshard.api.event.role;

import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.event.model.Event;

public interface RoleEvent extends Event {
    Role getRole();
}
