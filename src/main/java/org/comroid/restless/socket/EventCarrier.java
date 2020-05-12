package org.comroid.restless.socket;

import org.comroid.restless.socket.event.SocketEvent;

public final class EventCarrier {
    public final SocketEvent.Container Base;

    public EventCarrier(WebSocket webSocket) {
        this.Base = new SocketEvent.Container(webSocket);
    }
}
