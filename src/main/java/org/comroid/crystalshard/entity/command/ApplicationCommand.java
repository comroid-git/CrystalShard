package org.comroid.crystalshard.entity.command;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.model.command.ApplicationCommandOption;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class ApplicationCommand extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<ApplicationCommand> TYPE
            = BASETYPE.subGroup("application-command", ApplicationCommand::resolve);
    public static final VarBind<ApplicationCommand, Long, Long, Long> PARENT_APP
            = TYPE.createBind("application_id")
            .extractAs(StandardValueType.LONG)
            .build();
    public static final VarBind<ApplicationCommand, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<ApplicationCommand, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<ApplicationCommand, UniObjectNode, ApplicationCommandOption, Span<ApplicationCommandOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(ApplicationCommandOption.TYPE)
            .intoSpan()
            .build();

    private ApplicationCommand(Context context, UniObjectNode data) {
        super(context, data, EntityType.APPLICATION_COMMAND);
    }

    public static ApplicationCommand resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getApplicationCommand, ApplicationCommand::new);
    }
}
