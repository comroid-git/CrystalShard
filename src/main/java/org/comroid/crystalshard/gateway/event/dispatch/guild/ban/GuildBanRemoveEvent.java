package org.comroid.crystalshard.gateway.event.dispatch.guild.ban;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class GuildBanRemoveEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<GuildBanRemoveEvent> TYPE
            = BASETYPE.subGroup("guild-ban-remove", GuildBanRemoveEvent::new);

    public GuildBanRemoveEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
