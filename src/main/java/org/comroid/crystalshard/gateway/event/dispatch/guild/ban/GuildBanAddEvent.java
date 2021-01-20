package org.comroid.crystalshard.gateway.event.dispatch.guild.ban;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class GuildBanAddEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<GuildBanAddEvent> TYPE
            = BASETYPE.subGroup("guild-ban-add", GuildBanAddEvent::new);

    public GuildBanAddEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
