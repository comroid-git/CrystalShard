package org.comroid.restless.socket.event;

import org.comroid.common.func.Invocable;
import org.comroid.common.func.Provider;
import org.comroid.common.ref.Specifiable;
import org.comroid.common.ref.StaticCache;
import org.comroid.listnr.EventContainer;
import org.comroid.listnr.EventPayload;
import org.comroid.listnr.EventType;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * Any Discord Bot related Event
 */
public interface SocketEvent {
    interface Type<P extends Payload> extends EventType<UniObjectNode, WebSocket, P> {
    }

    interface Payload extends EventPayload<WebSocket, Type<Payload>>, Specifiable<Payload> {
        WebSocket getWebSocket();
    }

    class Container<P extends Payload> implements EventContainer<UniObjectNode, WebSocket, Type<P>, P> {
        private final WebSocket webSocket;
        private final Type type;

        public final WebSocket getWebSocket() {
            return webSocket;
        }

        @Override
        public Type getType() {
            return type;
        }

        public Container(WebSocket webSocket) {
            this.webSocket = webSocket;
            this.type = new TypeImpl(webSocket);
        }

        class TypeImpl extends EventType.Basic<UniObjectNode, WebSocket, Payload> implements Type {
            @Override
            public Invocable.TypeMap<? extends Payload> getInstanceSupplier() {
                return StaticCache.access(this, "instanceSupplier",
                        () -> Invocable.<Payload>ofMethodCall(this, "craftBasicPayload").typeMapped());
            }

            public Payload craftBasicPayload(UniObjectNode data) {
                return new PayloadImpl(this, data);
            }

            public TypeImpl(Collection<? extends EventType<UniObjectNode, WebSocket, ?>> parents, Class<Payload> payloadType, WebSocket dependent) {
                super(parents, payloadType, dependent);
            }

            public TypeImpl(WebSocket bot) {
                this(Collections.emptyList(), Payload.class, bot);
            }
        }

        class PayloadImpl extends EventPayload.Basic<WebSocket, Type> implements Payload {
            @Override
            public WebSocket getWebSocket() {
                return webSocket;
            }

            public PayloadImpl(TypeImpl masterEventType, @Nullable UniObjectNode data) {
                super(masterEventType, data, webSocket);
            }
        }
    }
}
