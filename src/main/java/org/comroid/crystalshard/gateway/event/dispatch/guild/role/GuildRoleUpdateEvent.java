package org.comroid.crystalshard.gateway.event.dispatch.guild.role;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class GuildRoleUpdateEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<GuildRoleUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-role-update", GuildRoleUpdateEvent::new);

    public GuildRoleUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
