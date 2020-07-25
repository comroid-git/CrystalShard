package org.comroid.crystalshard.core.gateway.payload.message;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayMessageUpdatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayMessageUpdatePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-message-update");
    public static final VarBind<UniObjectNode, DiscordBot, Message, Message> message
            = Root.createBind("")
            .extractAsObject()
            .andProvide(
                    Snowflake.Bind.ID,
                    (id, bot) -> bot.getSnowflake(Snowflake.Type.MESSAGE, id).get(),
                    Message.Bind.Root)
            .onceEach()
            .build();

    public Message getMessage() {
        return requireNonNull(message);
    }

    public GatewayMessageUpdatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
