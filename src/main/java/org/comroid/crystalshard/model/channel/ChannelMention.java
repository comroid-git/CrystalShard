package org.comroid.crystalshard.model.channel;

import org.comroid.common.func.Invocable;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.channel.GuildChannel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

@Location(ChannelMention.Bind.class)
public class ChannelMention extends BotBound.DataBase {
    protected ChannelMention(DiscordBot bot, @Nullable UniObjectNode initialData) {
        super(bot, initialData);
    }

    final class Bind {
        @RootBind
        GroupBind<ChannelMention, DiscordBot> Root
                = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "mention_channel", Invocable.ofConstructor(ChannelMention.class));
        VarBind.DependentTwoStage<Long, DiscordBot, GuildChannel> Channel
                = Root.bindDependent("id", ValueType.LONG, (bot, id) -> bot.getGuildChannelByID(id).requireNonNull());
        VarBind.DependentTwoStage<Long, DiscordBot, Guild> Guild
                = Root.bindDependent("guild_id", ValueType.LONG, (bot, id) -> bot.getGuildByID(id).requireNonNull());
        VarBind.TwoStage<Integer, Channel.Type> ChannelType
                = Root.bind2stage("type", ValueType.INTEGER, org.comroid.crystalshard.entity.channel.Channel.Type::valueOf);
        VarBind.OneStage<String> ChannelName
                = Root.bind1stage("name", ValueType.STRING);
    }
}
