package org.comroid.crystalshard.gateway.event.dispatch.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

public final class GuildCreateEvent extends GatewayEvent {
    public GuildCreateEvent(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData); // todo
    }
}
