package org.comroid.crystalshard.model.command;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.command.Command;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

public final class CommandInteractionData extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<CommandInteractionData> TYPE
            = BASETYPE.subGroup("application-command-interaction-data", CommandInteractionData::new);
    public static final VarBind<CommandInteractionData, Long, Command, Command> COMMAND
            = TYPE.createBind("id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((ev, id) -> ev.getCache().getApplicationCommand(id))
            .build();
    public static final VarBind<CommandInteractionData, String, String, String> COMMAND_NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<CommandInteractionData, UniObjectNode, CommandInteractionDataOption, Span<CommandInteractionDataOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(CommandInteractionDataOption.TYPE)
            .intoSpan()
            .build();
    public final Reference<Command> command = getComputedReference(COMMAND);
    public final Reference<String> commandName = getComputedReference(COMMAND_NAME);

    public Command getCommand() {
        return command.assertion();
    }

    public String getCommandName() {
        return commandName.assertion();
    }

    public Span<CommandInteractionDataOption> getOptions() {
        return getComputedReference(OPTIONS).orElseGet(Span::empty);
    }

    public CommandInteractionData(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
