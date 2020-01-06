package org.comroid.crystalshard.api.event.multipart.role;

import java.util.Collection;
import java.util.Optional;

import org.comroid.crystalshard.api.entity.guild.Role;
import org.comroid.crystalshard.api.entity.user.User;
import org.comroid.crystalshard.api.event.multipart.APIEvent;
import org.comroid.crystalshard.api.event.multipart.user.WrappedUserEvent;

import org.jetbrains.annotations.Nullable;

public interface WrappedRolesEvent extends WrappedUserEvent, RolesEvent, APIEvent {
    @Override
    Collection<Role> getRoles();

    @Override 
    default @Nullable User getUser() {
        return wrapUser().orElse(null);
    }

    @Override 
    Optional<User> wrapUser();
}
