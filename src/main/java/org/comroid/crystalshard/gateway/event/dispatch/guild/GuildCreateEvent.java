package org.comroid.crystalshard.gateway.event.dispatch.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildCreateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildCreateEvent> TYPE
            = BASETYPE.subGroup("guild-create", GuildCreateEvent::new);
    public static final VarBind<GuildCreateEvent, UniObjectNode, Guild, Guild> GUILD
            = TYPE.createBind("")
            .extractAsObject()
            .andResolve(Guild::resolve)
            .build();
    public final Reference<Guild> guild = getComputedReference(GUILD);

    public GuildCreateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
