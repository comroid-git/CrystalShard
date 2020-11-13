package org.comroid.crystalshard.entity.message;

import org.comroid.common.Polyfill;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.net.URL;

public interface MessageAttachment extends DiscordEntity {
    interface Bind extends DiscordEntity.Bind {
        GroupBind<MessageAttachment, DiscordBot> Root
                = DiscordEntity.Bind.Root.subGroup("message_attachment");
        VarBind.OneStage<String> Filename
                = Root.bind1stage("filename", ValueType.STRING);
        VarBind.OneStage<Integer> Size
                = Root.bind1stage("size", ValueType.INTEGER);
        VarBind.TwoStage<String, URL> Url
                = Root.bind2stage("url", ValueType.STRING, Polyfill::url);
        VarBind.TwoStage<String, URL> ProxyUrl
                = Root.bind2stage("proxy_url", ValueType.STRING, Polyfill::url);
        VarBind.OneStage<Integer> Height
                = Root.bind1stage("height", ValueType.INTEGER);
        VarBind.OneStage<Integer> Width
                = Root.bind1stage("width", ValueType.INTEGER);
    }
}
