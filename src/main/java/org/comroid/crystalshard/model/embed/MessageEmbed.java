package org.comroid.crystalshard.model.embed;

import org.comroid.common.Polyfill;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;

import java.awt.*;
import java.net.URL;
import java.time.Instant;

public interface MessageEmbed extends BotBound, DataContainer<DiscordBot> {
    interface Bind {
        GroupBind<MessageEmbed, DiscordBot> Root
                = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "embed");
        VarBind.OneStage<String> Title
                = Root.bind1stage("title", ValueType.STRING);
        VarBind.OneStage<String> Type
                = Root.bind1stage("type", ValueType.STRING);
        VarBind.OneStage<String> Description
                = Root.bind1stage("description", ValueType.STRING);
        VarBind.TwoStage<String, URL> URL
                = Root.bind2stage("url", ValueType.STRING, Polyfill::url);
        VarBind.TwoStage<String, Instant> Timestamp
                = Root.bind2stage("timestamp", ValueType.STRING, Instant::parse);
        VarBind.TwoStage<Integer, Color> Color
                = Root.bind2stage("color", ValueType.INTEGER, Color::new);
        VarBind.TwoStage<UniObjectNode, MessageEmbedFooter> Footer
                = Root.bind2stage("footer", MessageEmbedFooter.Bind.Root);
        //todo Incomplete
    }
}
