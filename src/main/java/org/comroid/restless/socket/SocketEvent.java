package org.comroid.restless.socket;

import old.Event;
import old.EventHub;
import old.EventType;

public final class SocketEvent {
    public static final class Container<O, E extends WebSocketEvent<? super E>> {
        private final WebSocket<O, E>                                           webSocket;
        private final EventHub<String, O, ? extends EventType<String, O, E>, E> eventHub;

        Container(
                WebSocket<O, E> webSocket,
                EventHub<String, O, ? extends EventType<String, O, E>, E> eventHub
        ) {
            this.webSocket = webSocket;
            this.eventHub  = eventHub;
        }

        public final class Generic extends Abstract<Generic> {
            private Generic(WebSocket<O, E> socket) {
                super(socket);
            }
        }

        private abstract class Abstract<S extends Abstract<S>> extends Event.Support.Abstract<S> implements Event<S> {
            private final WebSocket<O, E> socket;

            protected Abstract(EventType<?, ?, ? extends S> subtypes, WebSocket<O, E> socket) {
                super(subtypes);

                this.socket = socket;
            }

            private Abstract(WebSocket<O, E> socket) {
                this.socket = socket;
            }

            public WebSocket<O,E> getSocket() {
                return socket;
            }
        }
    }
}
