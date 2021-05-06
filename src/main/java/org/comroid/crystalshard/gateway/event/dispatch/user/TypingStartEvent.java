package org.comroid.crystalshard.gateway.event.dispatch.user;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
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

import java.time.Instant;

public final class TypingStartEvent extends DispatchEvent {
    @RootBind
    public static final GroupBind<TypingStartEvent> TYPE
            = BASETYPE.subGroup("typing-start", TypingStartEvent::new);
    public static final VarBind<TypingStartEvent, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getChannel(id))
            .build();
    public static final VarBind<TypingStartEvent, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getGuild(id))
            .build();
    public static final VarBind<TypingStartEvent, Long, User, User> USER
            = TYPE.createBind("user_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((event, id) -> event.getCache().getUser(id))
            .build();
    public static final VarBind<TypingStartEvent, Integer, Instant, Instant> STARTED
            = TYPE.createBind("timestamp")
            .extractAs(StandardValueType.INTEGER)
            .andRemap(Instant::ofEpochSecond)
            .build();
    public static final VarBind<TypingStartEvent, UniObjectNode, UniObjectNode, UniObjectNode> MEMBER
            = TYPE.createBind("member")
            .extractAsObject()
            .build(); // todo GuildMember
    public final Reference<Channel> channel = getComputedReference(CHANNEL);
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Reference<User> user = getComputedReference(USER);
    public final Reference<Instant> started = getComputedReference(STARTED);

    public Channel getChannel() {
        return channel.assertion();
    }

    public Guild getGuild() {
        return guild.assertion();
    }

    public User getUser() {
        return user.assertion();
    }

    public Instant getStartedTimestamp() {
        return started.assertion();
    }

    public TypingStartEvent(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
