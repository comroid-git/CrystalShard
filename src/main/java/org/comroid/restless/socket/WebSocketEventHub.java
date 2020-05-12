package org.comroid.restless.socket;

import org.comroid.listnr.EventContainer;
import org.comroid.listnr.ListnrCore;
import org.comroid.restless.socket.event.CloseEvent;
import org.comroid.restless.socket.event.DataEvent;
import org.comroid.restless.socket.event.OpenEvent;
import org.comroid.restless.socket.event.SocketEvent;
import org.comroid.uniform.node.UniObjectNode;

public final class WebSocketEventHub extends ListnrCore<UniObjectNode, WebSocket, SocketEvent.Type<?>> {
    @SuppressWarnings("rawtypes")
    public final SocketEvent.Container Base;
    public final EventContainer<UniObjectNode, WebSocket, SocketEvent.Type<OpenEvent.Payload>, OpenEvent.Payload> Open;
    public final EventContainer<UniObjectNode, WebSocket, SocketEvent.Type<DataEvent.Payload>, DataEvent.Payload> Data;
    public final EventContainer<UniObjectNode, WebSocket, SocketEvent.Type<CloseEvent.Payload>, CloseEvent.Payload> Close;

    public WebSocketEventHub(WebSocket webSocket) {
        super(webSocket);

        this.Base = SocketEvent.container(webSocket);
        this.Open = new OpenEvent.Container(webSocket).registerAt(this);
        this.Data = new DataEvent.Container(webSocket).registerAt(this);
        this.Close = new CloseEvent.Container(webSocket).registerAt(this);
    }
}
