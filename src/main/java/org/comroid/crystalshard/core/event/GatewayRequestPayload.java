package org.comroid.crystalshard.core.event;

import org.comroid.common.Polyfill;
import org.comroid.crystalshard.CrystalShard;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.uniform.node.UniValueNode;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.ReBind;
import org.comroid.varbind.bind.VarBind;
import org.comroid.varbind.container.DataContainer;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.net.URI;

@Internal
@Location(GatewayRequestPayload.Bind.class)
public interface GatewayRequestPayload extends DataContainer.Underlying<Object> {
    default URI getGatewayUri() {
        return ref(Bind.Uri).requireNonNull();
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
        @RootBind
        GroupBind<GatewayRequestPayload, Object> Root = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "gateway_bot_response", Basic.class);
        VarBind.OneStage<UniObjectNode> SessionStartLimit = Root.bind1stage("session_start_limit", (a, b) -> a);
        ReBind.TwoStage<UniObjectNode, Integer> SessionStartLimitRemaining = SessionStartLimit.rebindSimple(node -> node.get("remaining").asInt(0));
        ReBind.TwoStage<UniObjectNode, Integer> SessionStartLimitResetAfter = SessionStartLimit.rebindSimple(node -> node.get("reset_after").asInt(0));
        ReBind.TwoStage<UniObjectNode, Integer> SessionStartLimitTotal = SessionStartLimit.rebindSimple(node -> node.get("total").asInt(0));
        VarBind.OneStage<Integer> ShardCount = Root.bind1stage("shards", UniValueNode.ValueType.INTEGER);
        VarBind.TwoStage<String, URI> Uri = Root.bind2stage("url", UniValueNode.ValueType.STRING, Polyfill::uri);
    }

    final class Basic implements GatewayRequestPayload {
        private final DataContainer<Object> underlyingVarCarrier;

        @Override
        public final DataContainer<Object> getUnderlyingVarCarrier() {
            return underlyingVarCarrier;
        }

        public Basic(UniObjectNode initialData) {
            this.underlyingVarCarrier = new DataContainerBase<>(initialData, null, Basic.class);
        }
    }
}
