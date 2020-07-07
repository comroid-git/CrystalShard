package org.comroid.crystalshard.event;

import org.comroid.listnr.EventType;
import org.comroid.restless.socket.event.WebSocketPayload;

/**
 * Any Discord Bot related Event
 */
public abstract class BotEvent implements EventType<WebSocketPayload.Data, BotPayload> {
    private final String dispatchName;

    protected BotEvent(String dispatchName) {
        this.dispatchName = dispatchName;
    }

    @Override
    public boolean test(WebSocketPayload.Data data) {
        return data.getBody().get("t").asString().equals(dispatchName);
    }
}
