package org.comroid.crystalshard.rest.response;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Polyfill;
import org.comroid.crystalshard.model.DiscordDataContainer;
import org.comroid.mutatio.ref.Reference;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.util.StandardValueType;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public final class GatewayBotResponse extends AbstractRestResponse {
    @RootBind
    public static final GroupBind<GatewayBotResponse> TYPE
            = BASETYPE.subGroup("rest-gateway-bot-response", GatewayBotResponse::new);
    public static final VarBind<GatewayBotResponse, String, URI, URI> URL
            = TYPE.createBind("url")
            .extractAs(StandardValueType.STRING)
            .andRemap(Polyfill::uri)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<GatewayBotResponse, Integer, Integer, Integer> SHARDS
            = TYPE.createBind("shards")
            .extractAs(StandardValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<GatewayBotResponse, UniObjectNode, SessionStartLimit, SessionStartLimit> SESSION_START_LIMIT
            = TYPE.createBind("session_start_limit")
            .extractAsObject()
            .andResolve(SessionStartLimit::new)
            .onceEach()
            .setRequired()
            .build();
    public final Reference<URI> uri = getComputedReference(URL);
    public final Reference<Integer> shards = getComputedReference(SHARDS);
    public final Reference<SessionStartLimit> sessionStartLimit = getComputedReference(SESSION_START_LIMIT);

    public GatewayBotResponse(ContextualProvider context, @Nullable UniNode initialData) {
        super(context, initialData);
    }

    public static class SessionStartLimit extends DataContainerBase<DiscordDataContainer> implements DiscordDataContainer {
        @RootBind
        public static final GroupBind<SessionStartLimit> TYPE
                = BASETYPE.subGroup(GatewayBotResponse.TYPE, "session-start-limit", SessionStartLimit::new);
        public static final VarBind<SessionStartLimit, Integer, Integer, Integer> TOTAL
                = TYPE.createBind("total")
                .extractAs(StandardValueType.INTEGER)
                .asIdentities()
                .onceEach()
                .setRequired()
                .build();
        public static final VarBind<SessionStartLimit, Integer, Integer, Integer> REMAINING
                = TYPE.createBind("remaining")
                .extractAs(StandardValueType.INTEGER)
                .asIdentities()
                .onceEach()
                .setRequired()
                .build();
        public static final VarBind<SessionStartLimit, Integer, Integer, Integer> RESET_AFTER
                = TYPE.createBind("reset_after")
                .extractAs(StandardValueType.INTEGER)
                .asIdentities()
                .onceEach()
                .setRequired()
                .build();
        public final Reference<Integer> total = getComputedReference(TOTAL);
        public final Reference<Integer> remaining = getComputedReference(REMAINING);
        public final Reference<Integer> resetAfter = getComputedReference(RESET_AFTER);

        public int getTotal() {
            return total.assertion();
        }

        public int getRemaining() {
            return remaining.assertion();
        }

        public int getResetAfter() {
            return resetAfter.assertion();
        }

        public boolean isBlocked() {
            return getRemaining() != 0;
        }

        public SessionStartLimit(ContextualProvider context, @Nullable UniNode initialData) {
            super(context, initialData);
        }
    }
}
