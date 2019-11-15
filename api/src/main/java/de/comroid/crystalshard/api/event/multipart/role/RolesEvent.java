package de.comroid.crystalshard.api.event.multipart.role;

import java.util.Collection;

import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.event.multipart.APIEvent;
import de.comroid.crystalshard.api.event.multipart.user.UserEvent;

public interface RolesEvent extends UserEvent, APIEvent {
    Collection<Role> getRoles();
}
