package org.comroid.crystalshard.core.rest.payload;

import org.comroid.api.Invocable;
import org.comroid.api.Polyfill;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.AbstractDiscordResponsePayload;
import org.comroid.crystalshard.core.SessionStartLimit;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.net.URI;

@Location(GatewayBotRequestPayload.class)
public final class GatewayBotRequestPayload extends AbstractDiscordResponsePayload {
    @RootBind
    public static final GroupBind<GatewayBotRequestPayload, DiscordBot> Root
            = AbstractDiscordResponsePayload.Root.subGroup("gateway-bot", Invocable.ofConstructor(GatewayBotRequestPayload.class));
    public static final VarBind<Object, String, URI, URI> Uri
            = Root.createBind("url")
            .extractAs(ValueType.STRING)
            .andRemap(Polyfill::uri)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Integer, Integer, Integer> ShardCount
            = Root.createBind("shards")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, UniObjectNode, SessionStartLimit, SessionStartLimit> SessionLimit
            = Root.createBind("session_start_limit")
            .extractAsObject()
            .andConstruct(SessionStartLimit.Root)
            .onceEach()
            .setRequired()
            .build();

    public URI getWebSocketURI() {
        return requireNonNull(Uri);
    }

    public int getRecommendedShardCount() {
        return requireNonNull(ShardCount);
    }

    public SessionStartLimit getSessionStartLimit() {
        return requireNonNull(SessionLimit);
    }

    protected GatewayBotRequestPayload(UniObjectNode initialData) {
        super(null, initialData);
    }
}
