package org.comroid.crystalshard.gateway.event.dispatch.guild.integrations;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class GuildIntegrationsUpdateEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<GuildIntegrationsUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-integrations-update", GuildIntegrationsUpdateEvent::new);

    public GuildIntegrationsUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
