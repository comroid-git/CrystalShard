package org.comroid.crystalshard.gateway.event.dispatch.guild.ban;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.SimpleUser;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class GuildBanAddEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<GuildBanAddEvent> TYPE
            = BASETYPE.subGroup("guild-ban-add", GuildBanAddEvent::new);
    public static final VarBind<GuildBanAddEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public static final VarBind<GuildBanAddEvent, UniObjectNode, User, User> USER
            = TYPE.createBind("user")
            .extractAsObject()
            .andResolve(SimpleUser::resolve)
            .build();
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Reference<User> user = getComputedReference(USER);

    public Guild getGuild() {
        return guild.assertion();
    }

    public User getUser() {
        return user.assertion();
    }

    public GuildBanAddEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        if (!getGuild().setBannedState(getUser(), true))
            throw new IllegalStateException(String.format("Could not add ban for user in guild: %s in %s", getUser(), getGuild()));
    }
}
