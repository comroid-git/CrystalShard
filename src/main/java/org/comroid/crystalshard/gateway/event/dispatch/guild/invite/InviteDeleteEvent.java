package org.comroid.crystalshard.gateway.event.dispatch.guild.invite;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.jetbrains.annotations.Nullable;

public final class InviteDeleteEvent extends GatewayEvent {
    @RootBind
    public static final GroupBind<InviteDeleteEvent> TYPE
            = BASETYPE.subGroup("invite-delete", InviteDeleteEvent::new);

    public InviteDeleteEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
