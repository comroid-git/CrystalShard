package org.comroid.crystalshard.core.gateway.payload.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.GuildMember;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.mutatio.proc.Processor;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public final class GatewayTypingStartPayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayTypingStartPayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-typing-start");
    public static final VarBind<Object, UniObjectNode, Channel, Channel> channel
            = Root.createBind("channel_id")
            .extractAs(ValueType.LONG)
            .andConstruct(Channel.Bind.Root)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, UniObjectNode, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andConstruct(Guild.Bind.Root)
            .onceEach()
            .build();
    public static final VarBind<Object, UniObjectNode, User, User> user
            = Root.createBind("user_id")
            .extractAs(ValueType.LONG)
            .andConstruct(User.Bind.Root)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Integer, Instant, Instant> timestamp
            = Root.createBind("timestamp")
            .extractAs(ValueType.INTEGER)
            .andRemap(Instant::ofEpochSecond)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, UniObjectNode, GuildMember, GuildMember> member
            = Root.createBind("member")
            .extractAsObject()
            .andResolve((obj, bot) -> bot.updateGuildMember(obj))
            .onceEach()
            .build();

    public Channel getChannel() {
        return requireNonNull(channel);
    }

    public @Nullable
    Guild getGuild() {
        return get(guild);
    }

    public User getUser() {
        return requireNonNull(user);
    }

    public @Nullable
    GuildMember getMember() {
        return get(member);
    }

    public GatewayTypingStartPayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }

    public Processor<Guild> processGuild() {
        return process(guild);
    }

    public Instant startedTypingAt() {
        return requireNonNull(timestamp);
    }

    public Processor<GuildMember> processMember() {
        return process(member);
    }
}
