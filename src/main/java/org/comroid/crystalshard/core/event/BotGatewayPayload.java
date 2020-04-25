package org.comroid.crystalshard.core.event;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.comroid.common.Polyfill;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.event.DiscordBotEvent;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.varbind.GroupBind;
import org.comroid.varbind.ReBind;
import org.comroid.varbind.VarBind;
import org.comroid.varbind.VarBind.Location;
import org.comroid.varbind.VarBind.Root;

@Location(BotGatewayPayload.Bind.class)
public interface BotGatewayPayload extends DiscordBotEvent {
    default URI getGatewayUrl() {
        return ref(Bind.Url).process()
                .map((URL url) -> {
                    try {
                        return url.toURI();
                    } catch (URISyntaxException e) {
                        throw new AssertionError(e);
                    }
                })
                .requireNonNull();
    }

    default int getShardCount() {
        return requireNonNull(Bind.ShardCount);
    }

    default int getSessionStartLimitTotal() {
        return requireNonNull(Bind.SessionStartLimitTotal);
    }

    default int getSessionStartLimitRemaining() {
        return requireNonNull(Bind.SessionStartLimitRemaining);
    }

    default int getSessionStartLimitResetAfter() {
        return requireNonNull(Bind.SessionStartLimitResetAfter);
    }

    interface Bind {
        @Root GroupBind Root = new GroupBind(CrystalShard.SERIALIZATION_ADAPTER, "gateway_bot_response");
        VarBind.Uno<UniObjectNode>         SessionStartLimit           = Root.bind1stage("session_start_limit", (a, b) -> a);
        ReBind.Duo<UniObjectNode, Integer> SessionStartLimitRemaining  = SessionStartLimit.rebindSimple(node -> node.get(
                "remaining")
                .asInt(0));
        ReBind.Duo<UniObjectNode, Integer> SessionStartLimitResetAfter = SessionStartLimit.rebindSimple(node -> node.get(
                "reset_after")
                .asInt(0));
        ReBind.Duo<UniObjectNode, Integer> SessionStartLimitTotal      = SessionStartLimit.rebindSimple(node -> node.get("total")
                .asInt(0));
        VarBind.Uno<Integer>               ShardCount                  = Root.bind1stage("shards",
                UniValueNode.ValueType.INTEGER
        );
        VarBind.Duo<String, URL>           Url                         = Root.bind2stage("url",
                UniValueNode.ValueType.STRING,
                Polyfill::url
        );
    }

    final class Basic extends DiscordBotEvent.Abstract<BotGatewayPayload> implements BotGatewayPayload {
        public Basic(DiscordBot bot, UniObjectNode initialData) {
            super(bot, initialData);
        }
    }
}
