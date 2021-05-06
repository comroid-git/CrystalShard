package org.comroid.crystalshard.gateway.event.dispatch.message;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.gateway.event.dispatch.DispatchEvent;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class MessageDeleteBulkEvent extends DispatchEvent {
    public static final VarBind<MessageDeleteBulkEvent, Long, Long, Span<Long>> MESSAGE_IDS
            = TYPE.createBind("ids")
            .extractAsArray(StandardValueType.LONG)
            .asIdentities()
            .intoSpan()
            .build();
    @RootBind
    public static final GroupBind<MessageDeleteBulkEvent> TYPE
            = BASETYPE.subGroup("message-delete-bulk", MessageDeleteBulkEvent::new);
    public static final VarBind<MessageDeleteBulkEvent, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getChannel(id))
            .build();
    public static final VarBind<MessageDeleteBulkEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public final long[] ids;
    public final Reference<Channel> channel = getComputedReference(CHANNEL);
    public final Reference<Guild> guild = getComputedReference(GUILD);

    public Channel getChannel() {
        return channel.assertion();
    }

    public Guild getGuild() {
        return guild.assertion();
    }

    public MessageDeleteBulkEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);

        final SnowflakeCache cache = getCache();
        this.ids = getComputedReference(MESSAGE_IDS)
                .assertion()
                .stream()
                .mapToLong(x -> x)
                .peek(id -> cache.getReference(EntityType.MESSAGE, id).unset())
                .toArray();
    }
}
