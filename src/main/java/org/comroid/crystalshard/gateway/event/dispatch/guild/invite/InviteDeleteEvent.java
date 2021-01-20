package org.comroid.crystalshard.gateway.event.dispatch.guild.invite;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public final class InviteDeleteEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<InviteDeleteEvent> TYPE
            = BASETYPE.subGroup("invite-delete", InviteDeleteEvent::new);
    public static final VarBind<InviteDeleteEvent, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getChannel(id))
            .build();
    public static final VarBind<InviteDeleteEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public static final VarBind<InviteDeleteEvent, String, String, String> CODE
            = TYPE.createBind("code")
            .extractAs(StandardValueType.STRING)
            .build();

    public InviteDeleteEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo Add Invite functionality
    }
}
