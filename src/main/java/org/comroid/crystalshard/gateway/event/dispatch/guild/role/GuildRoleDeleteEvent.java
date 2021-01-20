package org.comroid.crystalshard.gateway.event.dispatch.guild.role;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.mutatio.ref.KeyedReference;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildRoleDeleteEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildRoleDeleteEvent> TYPE
            = BASETYPE.subGroup("guild-role-delete", GuildRoleDeleteEvent::new);
    public static final VarBind<GuildRoleDeleteEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public static final VarBind<GuildRoleDeleteEvent, Long, Long, Long> ROLE
            = TYPE.createBind("role_id")
            .extractAs(StandardValueType.LONG)
            .build();
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Role role;

    public Guild getGuild() {
        return guild.assertion();
    }

    public Role getRole() {
        return role;
    }

    public GuildRoleDeleteEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        SnowflakeCache cache = getCache();
        long id = ROLE.getFrom(initialData.asObjectNode());
        KeyedReference<String, Snowflake> ref = cache.getReference(EntityType.ROLE, id);
        this.role = ref.flatMap(Role.class).assertion("Role not found: " + id);
        ref.unset();
    }
}
