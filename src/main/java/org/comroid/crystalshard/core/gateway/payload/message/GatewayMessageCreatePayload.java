package org.comroid.crystalshard.core.gateway.payload.message;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.core.gateway.payload.AbstractGatewayPayload;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

public final class GatewayMessageCreatePayload extends AbstractGatewayPayload {
    @RootBind
    public static final GroupBind<GatewayMessageCreatePayload, DiscordBot> Root
            = BaseGroup.rootGroup("gateway-message-create");
    public static final VarBind<Object, UniObjectNode, Message, Message> message
            = Root.createBind("")
            .extractAsObject()
            .andProvide(
                    DiscordEntity.Bind.ID,
                    (id, bot) -> bot.getSnowflake(DiscordEntity.Type.MESSAGE, id).get(),
                    Message.Bind.Root)
            .onceEach()
            .build();

    public Message getMessage() {
        return requireNonNull(message);
    }

    public GatewayMessageCreatePayload(GatewayPayloadWrapper gpw) {
        super(gpw);
    }
}
