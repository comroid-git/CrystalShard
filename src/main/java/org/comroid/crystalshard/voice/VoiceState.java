package org.comroid.crystalshard.voice;

import org.comroid.common.info.MessageSupplier;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.guild.GuildMember;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.mutatio.proc.Processor;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class VoiceState extends BotBound.DataBase {
    @RootBind
    public static final GroupBind<DataBase, DiscordBot> Root
            = BaseGroup.rootGroup("voice-state");
    public static final VarBind<Long, DiscordBot, Guild, Guild> guild
            = Root.createBind("guild_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.GUILD, id)
                    .requireNonNull(MessageSupplier.format("Guild with ID %d not found", id)))
            .onceEach()
            .build();
    public static final VarBind<Long, DiscordBot, Channel, Channel> channel
            = Root.createBind("channel_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.CHANNEL, id).get())
            .onceEach()
            .build();
    public static final VarBind<Long, DiscordBot, User, User> user
            = Root.createBind("user_id")
            .extractAs(ValueType.LONG)
            .andResolve((id, bot) -> bot.getSnowflake(Snowflake.Type.USER, id)
                    .requireNonNull(MessageSupplier.format("User with ID %d not found", id)))
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<UniObjectNode, DiscordBot, GuildMember, GuildMember> member
            = Root.createBind("member")
            .extractAsObject()
            .andResolve((obj, bot) -> bot.updateGuildMember(obj))
            .onceEach()
            .build();
    public static final VarBind<String, DiscordBot, String, String> sessionId
            = Root.createBind("session_id")
            .extractAs(ValueType.STRING)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Boolean, DiscordBot, Boolean, Boolean> deafened
            = Root.createBind("deaf")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Boolean, DiscordBot, Boolean, Boolean> muted
            = Root.createBind("mute")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Boolean, DiscordBot, Boolean, Boolean> selfDeafened
            = Root.createBind("self_deaf")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Boolean, DiscordBot, Boolean, Boolean> selfMuted
            = Root.createBind("self_mute")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Boolean, DiscordBot, Boolean, Boolean> selfStreaming
            = Root.createBind("self_stream")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Boolean, DiscordBot, Boolean, Boolean> selfVideo
            = Root.createBind("self_video")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Boolean, DiscordBot, Boolean, Boolean> suppressed
            = Root.createBind("suppress")
            .extractAs(ValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .build();

    public @Nullable Guild getGuild() {
        return get(guild);
    }

    public Processor<Guild> processGuild() {
        return process(guild);
    }

    public @Nullable Channel getChannel() {
        return get(channel);
    }

    public Processor<Channel> processChannel() {
        return process(channel);
    }

    public User getUser() {
        return requireNonNull(user);
    }

    public @Nullable GuildMember getGuildMember() {
        return get(member);
    }

    public Processor<GuildMember> processGuildMember() {
        return process(member);
    }

    public String getSessionID() {
        return get(sessionId);
    }

    public boolean isDeafened() {
        return requireNonNull(deafened);
    }

    public boolean isMuted() {
        return requireNonNull(muted);
    }

    public boolean isSelfDeafened() {
        return requireNonNull(selfDeafened);
    }

    public boolean isSelfMuted() {
        return requireNonNull(selfMuted);
    }

    public boolean isSelfStreaming() {
        return requireNonNull(selfStreaming);
    }

    public boolean isSelfVideoing() {
        return requireNonNull(selfVideo);
    }

    public boolean isSuppressed() {
        return requireNonNull(suppressed);
    }

    public VoiceState(DiscordBot bot, UniObjectNode initialData) {
        super(bot, initialData);
    }
}
