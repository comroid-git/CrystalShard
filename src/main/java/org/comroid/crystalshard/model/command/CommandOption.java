package org.comroid.crystalshard.model.command;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntEnum;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CommandOption extends AbstractDataContainer implements Named, Described {
    @RootBind
    public static final GroupBind<CommandOption> TYPE
            = BASETYPE.subGroup("application-command-option", CommandOption::new);
    public static final VarBind<CommandOption, Integer, Type, Type> OPTION_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(Type::valueOf)
            .build();
    public static final VarBind<CommandOption, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<CommandOption, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<CommandOption, Boolean, Boolean, Boolean> IS_DEFAULT
            = TYPE.createBind("default")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<CommandOption, Boolean, Boolean, Boolean> IS_REQUIRED
            = TYPE.createBind("required")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<CommandOption, UniObjectNode, CommandOptionChoice<?>, Span<CommandOptionChoice<?>>> CHOICES
            = TYPE.createBind("choices")
            .extractAsArray()
            .andConstruct(CommandOptionChoice.TYPE)
            .intoSpan()
            .build();
    public static final VarBind<CommandOption, UniObjectNode, CommandOption, Span<CommandOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(TYPE)
            .intoSpan()
            .build();
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<String> description = getComputedReference(DESCRIPTION);

    @Override
    public String getName() {
        return name.assertion();
    }

    @Override
    public String getDescription() {
        return description.assertion();
    }

    public CommandOption(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public enum Type implements IntEnum {
        SUB_COMMAND(1),
        SUB_COMMAND_GROUP(2),
        STRING(3),
        INTEGER(4),
        BOOLEAN(5),
        USER(6),
        CHANNEL(7),
        ROLE(8);

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

        public static Type typeOf(Class<?> klass) {
            if (String.class.equals(klass))
                return STRING;
            if (boolean.class.equals(klass) || Boolean.class.equals(klass))
                return BOOLEAN;
            if (int.class.equals(klass) || Integer.class.equals(klass))
                return INTEGER;
            if (User.class.isAssignableFrom(klass))
                return USER;
            if (Channel.class.isAssignableFrom(klass))
                return CHANNEL;
            if (Role.class.isAssignableFrom(klass))
                return ROLE;
            throw new IllegalArgumentException("Unknown type: " + klass);
        }
    }
}
