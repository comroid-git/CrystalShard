package org.comroid.crystalshard.gateway.event.dispatch.guild.member;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class GuildMemberRemoveEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<GuildMemberRemoveEvent> TYPE
            = BASETYPE.subGroup("guild-member-remove", GuildMemberRemoveEvent::new);

    public GuildMemberRemoveEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
