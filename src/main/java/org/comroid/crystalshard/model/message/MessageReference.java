package org.comroid.crystalshard.model.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.DiscordDataContainer;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class MessageReference extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    @RootBind
    public static final GroupBind<MessageReference> TYPE
            = BASETYPE.subGroup("message-reference", MessageReference::new);
    public static final VarBind<MessageReference, Long, Message, Message> MESSAGE
            = TYPE.createBind("message_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((msgref, id) -> msgref.getCache().getMessage(id))
            .build();
    public static final VarBind<MessageReference, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((msgref, id) -> msgref.getCache().getChannel(id))
            .build();
    public static final VarBind<MessageReference, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((msgref, id) -> msgref.getCache().getGuild(id))
            .build();

    public MessageReference(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
