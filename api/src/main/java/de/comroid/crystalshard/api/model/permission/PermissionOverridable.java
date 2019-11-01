package de.comroid.crystalshard.api.model.permission;

import java.util.Optional;

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.util.model.TypeGroup;

public interface PermissionOverridable extends Snowflake, TypeGroup<PermissionOverridable> {
    default Optional<GuildMember> asGuildMember() {
        return as(GuildMember.class);
    }

    default Optional<Role> asRole() {
        return as(Role.class);
    }
}
