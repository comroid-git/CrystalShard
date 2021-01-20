package org.comroid.crystalshard.gateway.event.dispatch.guild.integrations;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.GuildCreateEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildIntegrationsUpdateEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildIntegrationsUpdateEvent> TYPE
            = BASETYPE.subGroup("guild-integrations-update", GuildIntegrationsUpdateEvent::new);
    public static final VarBind<GuildIntegrationsUpdateEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.requireFromContext(SnowflakeCache.class).getGuild(id))
            .build();
    public final Reference<Guild> guild = getComputedReference(GUILD);

    public Guild getGuild() {
        return guild.assertion();
    }

    public GuildIntegrationsUpdateEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        // todo Request updated integrations
    }
}
