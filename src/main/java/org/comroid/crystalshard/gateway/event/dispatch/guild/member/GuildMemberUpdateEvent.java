package org.comroid.crystalshard.gateway.event.dispatch.guild.member;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildMemberUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildMemberUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-member-update", GuildMemberUpdateEvent::new);
    public static final VarBind<GuildMemberUpdateEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();

    public GuildMemberUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo Add Guild Member functionality
    }
}
