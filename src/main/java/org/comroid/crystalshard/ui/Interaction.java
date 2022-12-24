package org.comroid.crystalshard.ui;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntegerAttribute;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.GuildMember;
import org.comroid.crystalshard.model.DiscordDataContainer;
import org.comroid.crystalshard.model.command.CommandInteractionData;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Interaction extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
    @RootBind
    public static final GroupBind<Interaction> TYPE
            = BASETYPE.subGroup("interaction", Interaction::new);
    public static final VarBind<Interaction, Long, Long, Long> ID
            = TYPE.createBind("id")
            .extractAs(StandardValueType.LONG)
            .build();
    public static final VarBind<Interaction, Integer, Type, Type> INTERACTION_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(Type::valueOf)
            .build();
    public static final VarBind<Interaction, UniObjectNode, CommandInteractionData, CommandInteractionData> DATA
            = TYPE.createBind("data")
            .extractAsObject()
            .andConstruct(CommandInteractionData.TYPE)
            .build();
    public static final VarBind<Interaction, Long, Guild, Guild> GUILD
            = TYPE.createBind("guild_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((ev, id) -> ev.getCache().getGuild(id))
            .build();
    public static final VarBind<Interaction, Long, Channel, Channel> CHANNEL
            = TYPE.createBind("channel_id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((ev, id) -> ev.getCache().getChannel(id))
            .build();
    public static final VarBind<Interaction, UniObjectNode, GuildMember, GuildMember> MEMBER
            = TYPE.createBind("member")
            .extractAsObject()
            .andResolve((context, data) -> GuildMember.resolve(context.requireNonNull(GUILD), data))
            .onceEach()
            .addDependency(GUILD)
            .build();
    public static final VarBind<Interaction, String, String, String> CONTINUATION_TOKEN
            = TYPE.createBind("token")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Interaction, Integer, Integer, Integer> VERSION
            = TYPE.createBind("version")
            .extractAs(StandardValueType.INTEGER)
            .build();
    public final Reference<Long> id = getComputedReference(ID);
    public final Reference<Type> type = getComputedReference(INTERACTION_TYPE);
    public final Reference<CommandInteractionData> data = getComputedReference(DATA);
    public final Reference<Guild> guild = getComputedReference(GUILD);
    public final Reference<Channel> channel = getComputedReference(CHANNEL);
    public final Reference<GuildMember> member = getComputedReference(MEMBER);
    public final Reference<String> continuationToken = getComputedReference(CONTINUATION_TOKEN);

    public long getId() {
        return id.assertion();
    }

    public Type getType() {
        return type.assertion();
    }

    public CommandInteractionData getData() {
        return data.assertion();
    }

    public Guild getGuild() {
        return guild.assertion();
    }

    public Channel getChannel() {
        return channel.assertion();
    }

    public GuildMember getMember() {
        return member.assertion();
    }

    public String getContinuationToken() {
        return continuationToken.assertion();
    }

    public Interaction(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public enum Type implements IntegerAttribute {
        PING(1),
        APPLICATION_COMMAND(2);

        private final int value;

        @Override
        public @NotNull Integer getValue() {
            return value;
        }

        Type(int value) {
            this.value = value;
        }

        public static Rewrapper<Type> valueOf(int value) {
            return IntegerAttribute.valueOf(value, Type.class);
        }
    }
}
