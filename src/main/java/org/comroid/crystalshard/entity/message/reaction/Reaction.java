package org.comroid.crystalshard.entity.message.reaction;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.crystalshard.model.emoji.Emoji;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;

public interface Reaction extends DataContainer<DiscordBot>, BotBound {
    interface Bind {
        GroupBind<Reaction, DiscordBot> Root
                = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "reaction");
        VarBind.OneStage<Integer> Count
                = Root.bind1stage("count", ValueType.INTEGER);
        VarBind.OneStage<Boolean> ByMe
                = Root.bind1stage("me", ValueType.BOOLEAN);
        VarBind.DependentTwoStage<UniObjectNode, DiscordBot, Emoji> Emoji
                = Root.bindDependent("emoji", DiscordBot::updateEmoji);
    }
}
