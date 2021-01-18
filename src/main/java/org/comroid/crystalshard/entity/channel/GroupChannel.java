package org.comroid.crystalshard.entity.channel;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GroupChannel extends AbstractTextChannel implements TextChannel {
    @RootBind
    public static final GroupBind<GroupChannel> TYPE
            = TextChannel.BASETYPE.rootGroup("group-channel");
    public static final VarBind<GroupChannel, UniObjectNode, User, Span<User>> RECIPIENTS
            = TYPE.createBind("recipients")
            .extractAsArray()
            .andResolve(User::resolve)
            .intoSpan()
            .setRequired()
            .build();
    public static final VarBind<GroupChannel, String, String, String> ICON_HASH // todo
            = TYPE.createBind("icon")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<GroupChannel, Long, User, User> OWNER
            = TYPE.createBind("owner_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((channel, id) -> channel.requireFromContext(SnowflakeCache.class).getUser(id))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<GroupChannel, Long, User, User> OWNER_APPLICATION
            = TYPE.createBind("application_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((channel, id) -> channel.requireFromContext(SnowflakeCache.class).getUser(id))
            .onceEach()
            .setRequired()
            .build();

    GroupChannel(ContextualProvider context, UniObjectNode data) {
        super(context, data, EntityType.GROUP_CHANNEL);
    }
}
