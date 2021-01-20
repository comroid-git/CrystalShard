package org.comroid.crystalshard.model.command;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
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

public final class ApplicationCommandInteractionDataOption extends AbstractDataContainer implements Named {
    @RootBind
    public static final GroupBind<ApplicationCommandInteractionDataOption> TYPE
            = BASETYPE.subGroup("application-command-interaction-data-option", ApplicationCommandInteractionDataOption::new);
    public static final VarBind<ApplicationCommandInteractionDataOption, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<ApplicationCommandInteractionDataOption, String, String, String> VALUE
            = TYPE.createBind("value")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<ApplicationCommandInteractionDataOption, UniObjectNode, ApplicationCommandInteractionDataOption, Span<ApplicationCommandInteractionDataOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(ApplicationCommandInteractionDataOption.TYPE)
            .intoSpan()
            .build();
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<String> value = getComputedReference(VALUE);

    @Override
    public String getName() {
        return name.assertion();
    }

    public String getValue() {
        return value.assertion();
    }

    public Span<ApplicationCommandInteractionDataOption> getOptions() {
        return getComputedReference(OPTIONS).orElseGet(Span::empty);
    }

    public ApplicationCommandInteractionDataOption(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }
}
