package org.comroid.crystalshard.core.event;

import org.comroid.api.Invocable;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.GatewayOPCode;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.restless.socket.event.WebSocketPayload;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.Location;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Location(GatewayPayloadWrapper.class)
public final class GatewayPayloadWrapper extends BotBound.DataBase {
    @RootBind
    public static final GroupBind<GatewayPayloadWrapper, DiscordBot> Root
            = BaseGroup.subGroup("gateway-payload-wrapper", Invocable.ofConstructor(GatewayPayloadWrapper.class));
    public static final VarBind<Integer, DiscordBot, GatewayOPCode, GatewayOPCode> OpCode
            = Root.createBind("op")
            .extractAs(ValueType.INTEGER)
            .andRemap(GatewayOPCode::valueOf)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<UniObjectNode, DiscordBot, UniObjectNode, UniObjectNode> EventData
            = Root.createBind("d")
            .extractAsObject()
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<Integer, DiscordBot, Integer, Integer> Sequence
            = Root.createBind("s")
            .extractAs(ValueType.INTEGER)
            .asIdentities()
            .onceEach()
            .build();
    public static final VarBind<String, DiscordBot, GatewayEventDefinition, GatewayEventDefinition> EventType
            = Root.createBind("t")
            .extractAs(ValueType.STRING)
            .andRemap(GatewayEventDefinition::valueOf)
            .onceEach()
            .build();

    private final @Nullable WebSocketPayload.Data data;

    public Optional<WebSocketPayload.Data> getData() {
        return Optional.ofNullable(data);
    }

    public GatewayPayloadWrapper(DiscordBot bot, WebSocketPayload.Data data) {
        super(bot, data.getBody().asObjectNode());

        this.data = data;
    }
}
