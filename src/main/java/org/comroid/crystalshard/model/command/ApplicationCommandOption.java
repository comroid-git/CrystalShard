package org.comroid.crystalshard.model.command;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntEnum;
import org.comroid.api.Rewrapper;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ApplicationCommandOption extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<ApplicationCommandOption> TYPE
            = BASETYPE.subGroup("application-command-option", ApplicationCommandOption::new);
    public static final VarBind<ApplicationCommandOption, Integer, Type, Type> OPTION_TYPE
            = TYPE.createBind("type")
            .extractAs(StandardValueType.INTEGER)
            .andRemapRef(Type::valueOf)
            .build();
    public static final VarBind<ApplicationCommandOption, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<ApplicationCommandOption, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<ApplicationCommandOption, Boolean, Boolean, Boolean> IS_DEFAULT
            = TYPE.createBind("default")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<ApplicationCommandOption, Boolean, Boolean, Boolean> IS_REQUIRED
            = TYPE.createBind("required")
            .extractAs(StandardValueType.BOOLEAN)
            .build();
    public static final VarBind<ApplicationCommandOption, UniObjectNode, ApplicationCommandOptionChoice<?>, Span<ApplicationCommandOptionChoice<?>>> CHOICES
            = TYPE.createBind("choices")
            .extractAsArray()
            .andConstruct(ApplicationCommandOptionChoice.TYPE)
            .intoSpan()
            .build();
    public static final VarBind<ApplicationCommandOption, UniObjectNode, ApplicationCommandOption, Span<ApplicationCommandOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(TYPE)
            .intoSpan()
            .build();

    public ApplicationCommandOption(ContextualProvider context, @Nullable UniNode initialData) {
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
    }
}
