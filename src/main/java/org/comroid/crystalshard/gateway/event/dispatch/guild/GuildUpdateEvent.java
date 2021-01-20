package org.comroid.crystalshard.gateway.event.dispatch.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-update", GuildUpdateEvent::new);
    public static final VarBind<GuildUpdateEvent, UniObjectNode, Guild, Guild> GUILD
            = TYPE.createBind("")
            .extractAsObject()
            .andResolve(Guild::resolve)
            .build();

    public GuildUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
