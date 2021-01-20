package org.comroid.crystalshard.gateway.event.dispatch.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class GuildUpdateEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<GuildUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-update", GuildUpdateEvent::new);

    public GuildUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
