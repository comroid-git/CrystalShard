package org.comroid.crystalshard.model.embed;

import org.comroid.common.Polyfill;
import org.comroid.common.info.Described;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.ArrayBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;

import java.awt.Color;
import java.net.URL;
import java.time.Instant;
import java.util.List;

public interface Embed extends Described, BotBound, DataContainer<DiscordBot> {
    interface Bind {
        GroupBind<Embed, DiscordBot> Root
                = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "embed");
        VarBind.OneStage<String> Title
                = Root.bind1stage("title", ValueType.STRING);
        VarBind.TwoStage<String, EmbedType> Type
                = Root.bind2stage("type", ValueType.STRING, EmbedType::valueOf);
        VarBind.OneStage<String> Description
                = Root.bind1stage("description", ValueType.STRING);
        VarBind.TwoStage<String, URL> URL
                = Root.bind2stage("url", ValueType.STRING, Polyfill::url);
        VarBind.TwoStage<String, Instant> Timestamp
                = Root.bind2stage("timestamp", ValueType.STRING, Instant::parse);
        VarBind.TwoStage<Integer, Color> Color
                = Root.bind2stage("color", ValueType.INTEGER, Color::new);
        VarBind.TwoStage<UniObjectNode, EmbedFooter> Footer
                = Root.bind2stage("footer", EmbedFooter.Bind.Root);
        VarBind.TwoStage<UniObjectNode, EmbedImage> Image
                = Root.bind2stage("image", EmbedImage.Bind.Root);
        VarBind.TwoStage<UniObjectNode, EmbedThumbnail> Thumbnail
                = Root.bind2stage("thumbnail", EmbedThumbnail.Bind.Root);
        VarBind.TwoStage<UniObjectNode, EmbedVideo> Video
                = Root.bind2stage("video", EmbedVideo.Bind.Root);
        VarBind.TwoStage<UniObjectNode, EmbedProvider> Provider
                = Root.bind2stage("provider", EmbedProvider.Bind.Root);
        VarBind.TwoStage<UniObjectNode, EmbedAuthor> Author
                = Root.bind2stage("author", EmbedAuthor.Bind.Root);
        ArrayBind.TwoStage<UniObjectNode, EmbedField, List<EmbedField>> Fields
                = Root.list2stage("fields", EmbedField.Bind.Root);
    }
}
