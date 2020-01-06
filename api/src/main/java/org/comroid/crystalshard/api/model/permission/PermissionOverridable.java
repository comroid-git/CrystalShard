package org.comroid.crystalshard.api.model.permission;

import java.util.Optional;

import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.crystalshard.api.entity.guild.Role;
import org.comroid.crystalshard.api.entity.user.GuildMember;
import org.comroid.crystalshard.util.model.TypeGroup;

public interface PermissionOverridable extends Snowflake, TypeGroup<PermissionOverridable> {
    default Optional<GuildMember> asGuildMember() {
        return as(GuildMember.class);
    }

    default Optional<Role> asRole() {
        return as(Role.class);
    }
}
