package org.comroid.crystalshard.model.guild;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class RoleTags extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    @RootBind
    public static final GroupBind<RoleTags> TYPE
            = BASETYPE.subGroup("role-tags", RoleTags::new);
    public static final VarBind<RoleTags, Long, User, User> BOT_ID
            = TYPE.createBind("bot_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((it, id) -> it.requireFromContext(Bot.class)
                    .getCache().getUser(id))
            .onceEach()
            .build();
    public static final VarBind<RoleTags, Long, GuildIntegration, GuildIntegration> INTEGRATION_ID
            = TYPE.createBind("integration_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((it, id) -> it.requireFromContext(Bot.class)
                    .getCache().getSnowflake(EntityType.GUILD_INTEGRATION, id))
            .onceEach()
            .build();
    public static final VarBind<RoleTags, Boolean, Boolean, Boolean> PREMIUM_SUBSCRIBER
            = TYPE.createBind("premium_subscriber")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();

    public RoleTags(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
