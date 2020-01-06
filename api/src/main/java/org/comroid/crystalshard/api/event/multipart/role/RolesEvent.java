package org.comroid.crystalshard.api.event.multipart.role;

import java.util.Collection;

import org.comroid.crystalshard.api.entity.guild.Role;
import org.comroid.crystalshard.api.event.multipart.APIEvent;
import org.comroid.crystalshard.api.event.multipart.user.UserEvent;

public interface RolesEvent extends UserEvent, APIEvent {
    Collection<Role> getRoles();
}
