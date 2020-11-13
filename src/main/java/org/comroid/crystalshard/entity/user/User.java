package org.comroid.crystalshard.entity.user;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.model.Mentionable;
import org.comroid.crystalshard.model.channel.PermissionOverride;
import org.comroid.crystalshard.model.message.MessageOperator;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;

public interface User extends DiscordEntity, Mentionable, MessageOperator, PermissionOverride.Settable {
    static User find(UniObjectNode data, DiscordBot bot) {
        // todo
    }

    interface Bind extends DiscordEntity.Bind {
        GroupBind<User, DiscordBot> Root = DiscordEntity.Bind.Root.subGroup("user", User.Basic.class);
    }
}
