package org.comroid.crystalshard.model.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class MessageReference extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<MessageReference> TYPE
            = BASETYPE.subGroup("message-reference");
    public static final VarBind<MessageReference, Long, Message, Message> MESSAGE
            = TYPE.createBind("message_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((msgref, id) -> msgref.requireFromContext(SnowflakeCache.class).getMessage(id))
            .build();
    public static final VarBind<MessageReference, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((msgref, id) -> msgref.requireFromContext(SnowflakeCache.class).getChannel(id))
            .build();
    public static final VarBind<MessageReference, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((msgref, id) -> msgref.requireFromContext(SnowflakeCache.class).getGuild(id))
            .build();

    public MessageReference(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
