package org.comroid.crystalshard.model.command;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.command.ApplicationCommand;
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

public final class ApplicationCommandInteractionData extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<ApplicationCommandInteractionData> TYPE
            = BASETYPE.subGroup("application-command-interaction-data", ApplicationCommandInteractionData::new);
    public static final VarBind<ApplicationCommandInteractionData, Long, ApplicationCommand, ApplicationCommand> COMMAND
            = TYPE.createBind("id")
            .extractAs(StandardValueType.LONG)
            .andResolveRef((ev, id) -> ev.getCache().getApplicationCommand(id))
            .build();
    public static final VarBind<ApplicationCommandInteractionData, String, String, String> COMMAND_NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<ApplicationCommandInteractionData, UniObjectNode, ApplicationCommandInteractionDataOption, Span<ApplicationCommandInteractionDataOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(ApplicationCommandInteractionDataOption.TYPE)
            .intoSpan()
            .build();
    public final Reference<ApplicationCommand> command = getComputedReference(COMMAND);
    public final Reference<String> commandName = getComputedReference(COMMAND_NAME);

    public ApplicationCommand getCommand() {
        return command.assertion();
    }

    public String getCommandName() {
        return commandName.assertion();
    }

    public Span<ApplicationCommandInteractionDataOption> getOptions() {
        return getComputedReference(OPTIONS).orElseGet(Span::empty);
    }

    public ApplicationCommandInteractionData(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
