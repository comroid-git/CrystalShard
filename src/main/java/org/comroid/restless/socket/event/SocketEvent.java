package org.comroid.restless.socket.event;

import org.comroid.common.Polyfill;
import org.comroid.common.func.Invocable;
import org.comroid.common.ref.Specifiable;
import org.comroid.listnr.model.EventContainer;
import org.comroid.listnr.model.EventPayload;
import org.comroid.listnr.model.EventType;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * Any Discord Bot related Event
 */
public interface SocketEvent {
    static <P extends Payload<? extends Type<P>>> SocketEvent.Container<P> container(WebSocket socket) {
        return new Container<>(socket);
    }

    interface Type<P extends Payload<? extends Type<P>>> extends EventType<UniObjectNode, WebSocket, P> {
    }

    interface Payload<T extends Type<? extends Payload<T>>> extends EventPayload<WebSocket, T>, Specifiable<Payload<T>> {
        WebSocket getWebSocket();
    }

    class Container<P extends Payload<? extends Type<P>>> implements EventContainer<UniObjectNode, WebSocket, Type<P>, P> {
        protected final CompletableFuture<Type<P>> typeFuture;
        private final WebSocket webSocket;
        private Type<P> type;

        public final WebSocket getWebSocket() {
            return webSocket;
        }

        @Override
        public Type<P> getType() {
            return type == null ? (type = typeFuture.join()) : type;
        }

        private Container(WebSocket webSocket) {
            this.webSocket = webSocket;
            this.typeFuture = null;
            this.type = new TypeImpl(webSocket) {
                private final Invocable.TypeMap<P> invoc = Invocable.<P>ofMethodCall(this, "craftBasicPayload").typeMapped();

                @Override
                public Invocable.TypeMap<? extends P> getInstanceSupplier() {
                    return invoc;
                }
            };
        }

        public Container(WebSocket webSocket, CompletableFuture<Type<P>> typeFuture) {
            this.webSocket = webSocket;
            this.typeFuture = typeFuture;
        }

        abstract class TypeImpl extends EventType.Basic<UniObjectNode, WebSocket, P> implements Type<P> {
            public TypeImpl(Collection<? extends EventType<?, ?, P>> parents, Class<P> payloadType, WebSocket dependent) {
                super(Polyfill.uncheckedCast(parents), payloadType, dependent);
            }

            public TypeImpl(WebSocket bot) {
                this(Collections.emptyList(), Polyfill.uncheckedCast(Payload.class), bot);
            }

            public <T extends Type<? extends Payload<T>>> Payload<T> craftBasicPayload(UniObjectNode data) {
                return new PayloadImpl<>(Polyfill.uncheckedCast(this), data);
            }
        }

        class PayloadImpl<XT extends Type<? extends Payload<XT>>> extends EventPayload.Basic<WebSocket, XT> implements Payload<XT> {
            @Override
            public WebSocket getWebSocket() {
                return webSocket;
            }

            public PayloadImpl(XT masterEventType, @Nullable UniObjectNode data) {
                super(masterEventType, data, webSocket);
            }
        }
    }
}
