package org.comroid.crystalshard.model.command;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.model.DiscordDataContainer;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class CommandInteractionDataOption extends DataContainerBase<DiscordDataContainer> implements Named, DiscordDataContainer {
    @RootBind
    public static final GroupBind<CommandInteractionDataOption> TYPE
            = BASETYPE.subGroup("application-command-interaction-data-option", CommandInteractionDataOption::new);
    public static final VarBind<CommandInteractionDataOption, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<CommandInteractionDataOption, Object, Object, Object> VALUE
            = TYPE.createBind("value")
            .extractAs(StandardValueType.OBJECT)
            .build();
    public static final VarBind<CommandInteractionDataOption, UniObjectNode, CommandInteractionDataOption, Span<CommandInteractionDataOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(CommandInteractionDataOption.TYPE)
            .intoSpan()
            .build();
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<Object> value = getComputedReference(VALUE);

    @Override
    public String getName() {
        return name.assertion();
    }

    public Object getValue() {
        return value.assertion();
    }

    public Span<CommandInteractionDataOption> getOptions() {
        return getComputedReference(OPTIONS).orElseGet(Span::empty);
    }

    public CommandInteractionDataOption(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
