package org.comroid.crystalshard.entity.channel;

import org.comroid.common.ref.Named;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.channel.PermissionOverride;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.ArrayBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.util.ArrayList;
import java.util.Collection;

public interface GuildChannel extends Channel, Named {
    @Override
    default String getDefaultFormattedName() {
        return getName();
    }

    @Override
    default String getAlternateFormattedName() {
        return getMentionTag();
    }

    interface Bind extends Channel.Bind {
        GroupBind<GuildChannel, DiscordBot> Root
                = Channel.Bind.Root.subGroup("guild_channel");
        VarBind.DependentTwoStage<Long, DiscordBot, Guild> Guild
                = Root.bindDependent("guild_id", ValueType.LONG, (bot, id) -> bot.getGuildByID(id).requireNonNull());
        VarBind.OneStage<Integer> Position
                = Root.bind1stage("position", ValueType.INTEGER);
        ArrayBind.DependentTwoStage<UniObjectNode, DiscordBot, PermissionOverride, Collection<PermissionOverride>> PermissionOverrides
                = Root.listDependent("permission_overwrites", DiscordBot::makeOverwrite, ArrayList::new);
        VarBind.OneStage<String> Name
                = Root.bind1stage("name", ValueType.STRING);
    }
}
