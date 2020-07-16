package org.comroid.crystalshard.event;

import org.comroid.crystalshard.core.event.GatewayPayloadWrapper;
import org.comroid.listnr.EventType;

public interface DiscordBotEvent extends EventType<GatewayPayloadWrapper, DiscordBotPayload> {
}
