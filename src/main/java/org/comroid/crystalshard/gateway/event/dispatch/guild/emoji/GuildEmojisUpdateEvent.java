package org.comroid.crystalshard.gateway.event.dispatch.guild.emoji;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class GuildEmojisUpdateEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<GuildEmojisUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-emojis-update", GuildEmojisUpdateEvent::new);

    public GuildEmojisUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
