package org.comroid.crystalshard.gateway.event.dispatch.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.crystalshard.gateway.event.dispatch.guild.emoji.GuildEmojisUpdateEvent;
import org.comroid.mutatio.ref.KeyedReference;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildDeleteEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildDeleteEvent> TYPE
            = BASETYPE.subGroup("guild-delete", GuildDeleteEvent::new);
    public static final VarBind<GuildDeleteEvent, UniObjectNode, UniObjectNode, UniObjectNode> GUILD_OBJ
            = TYPE.createBind("")
            .extractAsObject()
            .build();
    private final boolean wasKicked;
    private final Guild guild;

    public boolean wasKicked() {
        return wasKicked;
    }

    public Guild getGuild() {
        return guild;
    }

    public GuildDeleteEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        UniObjectNode data = getComputedReference(GUILD_OBJ).assertion();
        this.wasKicked = !data.containsKey(Guild.UNAVAILABLE.getFieldName());
        SnowflakeCache cache = context.getCache();
        long id = Snowflake.ID.getFrom(initialData.asObjectNode());
        KeyedReference<String, Snowflake> ref = cache.getReference(EntityType.GUILD, id);
        this.guild = ref.flatMap(Guild.class).assertion("Guild not found: " + id);
        ref.unset();
    }
}
