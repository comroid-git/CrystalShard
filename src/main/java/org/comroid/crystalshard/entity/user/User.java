package org.comroid.crystalshard.entity.user;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.Mentionable;
import org.comroid.crystalshard.model.channel.PermissionOverride;
import org.comroid.crystalshard.model.message.MessageOperator;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;

public interface User extends Snowflake, Mentionable, MessageOperator, PermissionOverride.Settable {
    static User find(UniObjectNode data, DiscordBot bot) {
        // todo
    }

    interface Bind extends Snowflake.Bind {
        GroupBind<User, DiscordBot> Root = Snowflake.Bind.Root.subGroup("user", User.Basic.class);
    }
}
