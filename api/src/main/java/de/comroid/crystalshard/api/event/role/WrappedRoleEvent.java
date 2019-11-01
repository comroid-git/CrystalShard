package de.comroid.crystalshard.api.event.role;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.guild.Role;

import org.jetbrains.annotations.Nullable;

public interface WrappedRoleEvent extends RoleEvent {
    @Override
    @Nullable Role getRole();

    default Optional<Role> wrapRole() {
        return Optional.ofNullable(getRole());
    }
}
