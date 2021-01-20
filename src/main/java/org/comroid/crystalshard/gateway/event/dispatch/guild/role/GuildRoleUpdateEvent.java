package org.comroid.crystalshard.gateway.event.dispatch.guild.role;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildRoleUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildRoleUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-role-update", GuildRoleUpdateEvent::new);
    public static final VarBind<GuildRoleUpdateEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public static final VarBind<GuildRoleUpdateEvent, UniObjectNode, Role, Role> ROLE
            = TYPE.createBind("role")
            .extractAsObject()
            .andResolve(Role::resolve)
            .build();
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Reference<Role> role = getComputedReference(ROLE);

    public Guild getGuild() {
        return guild.assertion();
    }

    public Role getRole() {
        return role.assertion();
    }

    public GuildRoleUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
