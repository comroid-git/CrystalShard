package org.comroid.crystalshard.core.event;

import java.net.URI;

import org.comroid.common.Polyfill;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.event.DiscordBotEvent;
import org.comroid.listnr.Event;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.varbind.GroupBind;
import org.comroid.varbind.ReBind;
import org.comroid.varbind.VarBind;
import org.comroid.varbind.VarBind.Location;
import org.comroid.varbind.VarBind.Root;
import org.comroid.varbind.VarCarrier;
import org.comroid.varbind.VariableCarrier;

@Location(BotGatewayPayload.Bind.class)
public interface BotGatewayPayload extends Event<DiscordBotEvent>, VarCarrier.Underlying<Object> {
    default URI getGatewayUrl() {
        return ref(Bind.Uri).process()
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
        VarBind.Duo<String, URI>           Uri                         = Root.bind2stage("url",
                UniValueNode.ValueType.STRING,
                Polyfill::uri
        );
    }

    final class Basic extends Event.Support.Abstract<DiscordBotEvent> implements BotGatewayPayload {
        private final VariableCarrier<Object> underlyingVarCarrier;

        public Basic(UniObjectNode initialData) {
            this.underlyingVarCarrier = new VariableCarrier<>(Basic.class, CrystalShard.SERIALIZATION_ADAPTER, initialData, null);
        }

        @Override
        public final VarCarrier<Object> getUnderlyingVarCarrier() {
            return underlyingVarCarrier;
        }
    }
}
