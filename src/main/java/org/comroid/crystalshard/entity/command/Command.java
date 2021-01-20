package org.comroid.crystalshard.entity.command;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Context;
import org.comroid.crystalshard.entity.EntityType;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.crystalshard.model.command.CommandOption;
import org.comroid.mutatio.span.Span;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class Command extends Snowflake.Abstract {
    @RootBind
    public static final GroupBind<Command> TYPE
            = BASETYPE.subGroup("application-command", Command::resolve);
    public static final VarBind<Command, Long, Long, Long> PARENT_APP
            = TYPE.createBind("application_id")
            .extractAs(StandardValueType.LONG)
            .build();
    public static final VarBind<Command, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Command, String, String, String> DESCRIPTION
            = TYPE.createBind("description")
            .extractAs(StandardValueType.STRING)
            .build();
    public static final VarBind<Command, UniObjectNode, CommandOption, Span<CommandOption>> OPTIONS
            = TYPE.createBind("options")
            .extractAsArray()
            .andConstruct(CommandOption.TYPE)
            .intoSpan()
            .build();

    private Command(Context context, UniObjectNode data) {
        super(context, data, EntityType.APPLICATION_COMMAND);
    }

    public static Command resolve(ContextualProvider context, UniNode data) {
        return Snowflake.resolve(context, data, SnowflakeCache::getApplicationCommand, Command::new);
    }
}
