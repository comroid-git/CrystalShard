package org.comroid.crystalshard.rest.response;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Polyfill;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public final class GatewayBotResponse extends AbstractDataContainer {
    @RootBind
    public static final GroupBind<GatewayBotResponse> TYPE
            = BASETYPE.rootGroup("rest-gateway-bot-response");
    public static final VarBind<GatewayBotResponse, String, URI, URI> URL
            = TYPE.createBind("url")
            .extractAs(ValueType.STRING)
            .andRemap(Polyfill::uri)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<GatewayBotResponse, Integer, Integer, Integer> SHARDS
            = TYPE.createBind("shards")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<GatewayBotResponse, UniObjectNode, SessionStartLimit, SessionStartLimit> SESSION_START_LIMIT
            = TYPE.createBind("session_start_limit")
            .extractAsObject()
            .andConstruct(SessionStartLimit.TYPE)
            .onceEach()
            .setRequired()
            .build();
    public final Reference<URI> uri = getComputedReference(URL);
    public final Reference<Integer> shards = getComputedReference(SHARDS);
    public final Reference<SessionStartLimit> sessionStartLimit = getComputedReference(SESSION_START_LIMIT);

    public GatewayBotResponse(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }

    public static class SessionStartLimit extends AbstractDataContainer {
        @RootBind
        public static final GroupBind<SessionStartLimit> TYPE
                = BASETYPE.rootGroup(GatewayBotResponse.TYPE, "session-start-limit");
        public static final VarBind<SessionStartLimit, Integer, Integer, Integer> TOTAL
                = TYPE.createBind("total")
                .extractAs(ValueType.INTEGER)
                .asIdentities()
                .onceEach()
                .setRequired()
                .build();
        public static final VarBind<SessionStartLimit, Integer, Integer, Integer> REMAINING
                = TYPE.createBind("remaining")
                .extractAs(ValueType.INTEGER)
                .asIdentities()
                .onceEach()
                .setRequired()
                .build();
        public static final VarBind<SessionStartLimit, Integer, Integer, Integer> RESET_AFTER
                = TYPE.createBind("reset_after")
                .extractAs(ValueType.INTEGER)
                .asIdentities()
                .onceEach()
                .setRequired()
                .build();
        public final Reference<Integer> total = getComputedReference(TOTAL);
        public final Reference<Integer> remaining = getComputedReference(REMAINING);
        public final Reference<Integer> resetAfter = getComputedReference(RESET_AFTER);

        public SessionStartLimit(ContextualProvider context, @Nullable UniObjectNode initialData) {
            super(context, initialData);
        }
    }
}
