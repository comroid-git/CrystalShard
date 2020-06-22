package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.util.Optional;

public interface PrivateChannel extends Channel {
    default Optional<String> getIconHash() {
        return wrap(Bind.IconHash);
    }

    default Optional<User> getOwnerUser() {
        return wrap(Bind.OwnerUser);
    }

    default Optional<SnowflakeSelector> getOwnerApplication() {
        return wrap(Bind.OwnerApplication);
    }

    interface Bind extends Channel.Bind {
        GroupBind<PrivateChannel, DiscordBot> Root = Channel.Bind.Root.subGroup("private_channel");
        VarBind.OneStage<String> IconHash
                = Root.bind1stage("icon", ValueType.STRING);
        VarBind.DependentTwoStage<Long, DiscordBot, User> OwnerUser
                = Root.bindDependent("owner_id", ValueType.LONG, (bot, id) -> bot.getUserByID(id).get());
        VarBind.DependentTwoStage<Long, DiscordBot, SnowflakeSelector> OwnerApplication
                = Root.bindDependent("application_id", ValueType.LONG, (bot, id) -> bot.getSnowflakesByID(id).get());
    }
}
