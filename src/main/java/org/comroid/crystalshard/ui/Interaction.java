package org.comroid.crystalshard.ui;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntEnum;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.command.ApplicationCommand;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.crystalshard.model.command.ApplicationCommandInteractionData;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Interaction extends AbstractDataContainer {
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
    public static final VarBind<Interaction, UniObjectNode, ApplicationCommandInteractionData, ApplicationCommandInteractionData> DATA
            = TYPE.createBind("data")
            .extractAsObject()
            .andConstruct(ApplicationCommandInteractionData.TYPE)
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
    public static final VarBind<Interaction, UniObjectNode, UniObjectNode, UniObjectNode> MEMBER
            = TYPE.createBind("member")
            .extractAsObject()
            .build(); // todo GuildMember
    public static final VarBind<Interaction, String, String, String> CONTINUATION_TOKEN
            = TYPE.createBind("token")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Interaction, Integer, Integer, Integer> VERSION
            = TYPE.createBind("version")
            .extractAs(StandardValueType.INTEGER)
            .build();

    public Interaction(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public enum Type implements IntEnum {
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
            return IntEnum.valueOf(value, Type.class);
        }
    }
}
