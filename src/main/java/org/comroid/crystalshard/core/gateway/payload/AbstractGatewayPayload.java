package org.comroid.crystalshard.core.gateway.payload;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.core.gateway.event.GatewayEvent;
import org.comroid.crystalshard.core.gateway.event.GatewayPayload;
import org.comroid.crystalshard.core.gateway.event.GatewayPayloadWrapper;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.listnr.EventPayload;
import org.comroid.varbind.bind.GroupBind;

public abstract class AbstractGatewayPayload extends BotBound.DataBase implements GatewayPayload {
    public static final GroupBind<AbstractGatewayPayload, DiscordBot> BaseGroup
            = DataBase.BaseGroup.subGroup("gateway-payload");
    private final GatewayEvent<? extends GatewayPayload> type;

    @Override
    public GatewayEvent<? extends GatewayPayload> getEventType() {
        return type;
    }

    protected AbstractGatewayPayload(GatewayPayloadWrapper gpw) {
        super(gpw.getBot(), gpw.wrap(GatewayPayloadWrapper.EventData).orElse(null));

        this.type = gpw.requireNonNull(GatewayPayloadWrapper.EventType);
    }
}
