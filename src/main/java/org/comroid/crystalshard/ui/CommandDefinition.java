package org.comroid.crystalshard.ui;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.crystalshard.model.command.CommandOption;
import org.comroid.crystalshard.ui.annotation.SlashCommand;
import org.comroid.mutatio.ref.Reference;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.lang.reflect.Method;

class CommandDefinition extends AbstractDataContainer implements Named, Described {
    @RootBind
    public static final GroupBind<CommandDefinition> TYPE
            = BASETYPE.subGroup("command-definition");
    public static final VarBind<CommandDefinition, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<CommandDefinition, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<CommandDefinition, UniObjectNode, CommandOption, Span<CommandOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(CommandOption.TYPE)
            .intoSpan()
            .build();
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<String> description = getComputedReference(DESCRIPTION);
    private final Object target;
    private final Method method;
    private final SlashCommand annotation;

    @Override
    public String getName() {
        return name.assertion();
    }

    @Override
    public String getDescription() {
        return description.assertion();
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public boolean useGlobally() {
        return annotation.useGlobally();
    }

    CommandDefinition(InteractionCore core, UniObjectNode data, Object target, Method method) {
        super(core, data);

        this.target = target;
        this.method = method;
        this.annotation = method.getAnnotation(SlashCommand.class);
    }
}
