package org.comroid.crystalshard.model.command;

import org.comroid.api.ContextualProvider;
import org.comroid.api.IntegerAttribute;
import org.comroid.api.Named;
import org.comroid.api.Rewrapper;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class CommandOption extends DataContainerBase<DiscordDataContainer> implements Named, Described, DiscordDataContainer {
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
    public static final VarBind<CommandOption, UniObjectNode, CommandOptionChoice<?>, List<CommandOptionChoice<?>>> CHOICES
            = TYPE.createBind("choices")
            .extractAsArray()
            .andConstruct(CommandOptionChoice.TYPE)
            .intoCollection((Supplier<List<CommandOptionChoice<?>>>) ArrayList::new)
            .setDefaultValue(Collections::emptyList)
            .build();
    public static final VarBind<CommandOption, UniObjectNode, CommandOption, List<CommandOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(TYPE)
            .intoCollection((Supplier<List<CommandOption>>) ArrayList::new)
            .setDefaultValue(Collections::emptyList)
            .build();
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<String> description = getComputedReference(DESCRIPTION);
    public final Reference<Boolean> isDefault = getComputedReference(IS_DEFAULT);
    public final Reference<Boolean> isRequired = getComputedReference(IS_REQUIRED);

    @Override
    public String getName() {
        return name.assertion();
    }

    @Override
    public String getDescription() {
        return description.assertion();
    }

    public boolean isDefault() {
        return isDefault.orElse(false);
    }

    public boolean isRequired() {
        return isRequired.orElse(false);
    }

    public List<CommandOptionChoice<?>> getChoices() {
        return getComputedReference(CHOICES).assertion();
    }

    public List<CommandOption> getOptions() {
        return getComputedReference(OPTIONS).assertion();
    }

    public CommandOption(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CommandOption))
            return false;
        final CommandOption other = (CommandOption) o;
        final Map<String, CommandOption> otherOptions = other.getOptions()
                .stream()
                .collect(Collectors.toMap(CommandOption::getName, Function.identity()));
        return name.equals(other.name)
                && description.equals(other.description)
                && isDefault.equals(other.isDefault)
                && isRequired.equals(other.isRequired)
                && getOptions().stream()
                .allMatch(opt -> otherOptions.containsKey(opt.getName())
                        && otherOptions.get(opt.getName()).equals(opt))
                && getChoices().stream()
                .allMatch(choice -> other.getChoices().stream().anyMatch(choice::equals));
    }

    public enum Type implements IntegerAttribute {
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
            return IntegerAttribute.valueOf(value, Type.class);
        }

        public static Type typeOf(Class<?> klass) {
            if (String.class.equals(klass))
                return STRING;
            if (boolean.class.equals(klass) || Boolean.class.equals(klass))
                return BOOLEAN;
            if (int.class.equals(klass) || Integer.class.equals(klass) || IntegerAttribute.class.isAssignableFrom(klass))
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
