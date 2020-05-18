package org.comroid.crystalshard.model.message;

import org.comroid.common.info.Described;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.guild.Role;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.ArrayBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;

import java.util.HashSet;
import java.util.Set;

public interface MessageMentions extends DataContainer<DiscordBot>, BotBound {
    enum Type implements Described {
        ROLES("Controls role mentions"),
        USERS("Controls user mentions"),
        EVERYONE("Controls @everyone and @here mentions");

        private final String description;

        @Override
        public String getDescription() {
            return description;
        }

        Type(String description) {
            this.description = description;
        }
    }

    interface Bind {
        GroupBind<MessageMentions, DiscordBot> Root
                = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "allowed_mentions");
        ArrayBind.TwoStage<String, MessageMentions.Type, Set<MessageMentions.Type>> MentionTypes
                = Root.list2stage("parse", ValueType.STRING, Type::valueOf, HashSet::new);
        ArrayBind.DependentTwoStage<Long, DiscordBot, Role, Set<Role>> Roles
                = Root.listDependent("roles", ValueType.LONG, (bot, id) -> bot.getRoleByID(id).get(), HashSet::new);
        ArrayBind.DependentTwoStage<Long, DiscordBot, User, Set<User>> Users
                = Root.listDependent("users", ValueType.LONG, (bot, id) -> bot.getUserByID(id).get(), HashSet::new);
    }
}
