package de.comroid.crystalshard.api.event.role;

import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.event.model.Event;

public interface RoleEvent extends Event {
    Role getRole();
}
