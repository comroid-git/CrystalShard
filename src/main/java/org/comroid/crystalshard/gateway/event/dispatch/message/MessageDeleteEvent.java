package org.comroid.crystalshard.gateway.event.dispatch.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.mutatio.ref.KeyedReference;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class MessageDeleteEvent extends DispatchEvent {
    public static final VarBind<MessageDeleteEvent, Long, Long, Long> MESSAGE_ID
            = TYPE.createBind("id")
            .extractAs(StandardValueType.LONG)
            .build();
    @RootBind
    public static final GroupBind<MessageDeleteEvent> TYPE
            = BASETYPE.subGroup("message-delete", MessageDeleteEvent::new);
    public static final VarBind<MessageDeleteEvent, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getChannel(id))
            .build();
    public static final VarBind<MessageDeleteEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public final Message message;
    public final Reference<Channel> channel = getComputedReference(CHANNEL);
    public final Reference<Guild> guild = getComputedReference(GUILD);

    public Message getMessage() {
        return message;
    }

    public Channel getChannel() {
        return channel.assertion();
    }

    public Guild getGuild() {
        return guild.assertion();
    }

    public MessageDeleteEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        SnowflakeCache cache = getCache();
        long id = MESSAGE_ID.getFrom(initialData.asObjectNode());
        KeyedReference<String, Snowflake> ref = cache.getReference(EntityType.MESSAGE, id);
        this.message = ref.flatMap(Message.class).assertion("Message not found: " + id);
        ref.unset();
    }
}
