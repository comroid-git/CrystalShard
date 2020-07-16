package org.comroid.crystalshard.core.gateway.payload;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.EventPayload;
import org.comroid.varbind.bind.GroupBind;

public abstract class AbstractGatewayPayload extends BotBound.DataBase implements EventPayload {
    public static final GroupBind<AbstractGatewayPayload, DiscordBot> BaseGroup
            = DataBase.BaseGroup.subGroup("gateway-payload");

    protected AbstractGatewayPayload(GatewayPayloadWrapper gpw) {
        super(gpw.getBot(), gpw.wrap(GatewayPayloadWrapper.EventData).orElse(null));
    }
}
