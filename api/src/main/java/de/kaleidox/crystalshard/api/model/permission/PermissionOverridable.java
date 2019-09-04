package de.kaleidox.crystalshard.api.model.permission;

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.guild.Role;
import de.kaleidox.crystalshard.api.entity.user.GuildMember;
import de.kaleidox.crystalshard.util.model.TypeGroup;

public interface PermissionOverridable extends TypeGroup<PermissionOverridable> {
    default Optional<GuildMember> asGuildMember() {
        return as(GuildMember.class);
    }

    default Optional<Role> asRole() {
        return as(Role.class);
    }
}
