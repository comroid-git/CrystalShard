package org.comroid.crystalshard.gateway.event.dispatch.guild.role;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class GuildRoleCreateEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<GuildRoleCreateEvent> TYPE
            = BASETYPE.subGroup("guild-role-create", GuildRoleCreateEvent::new);

    public GuildRoleCreateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
