package org.comroid.restless.socket.event;

import org.comroid.common.func.Invocable;
import org.comroid.common.ref.Specifiable;
import org.comroid.common.ref.StaticCache;
import org.comroid.listnr.EventContainer;
import org.comroid.listnr.EventPayload;
import org.comroid.listnr.EventType;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.node.UniObjectNode;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;

public interface OpenEvent {
    /*
    we need these classes to extend the SocketEvent classes
     */

    interface Type extends SocketEvent.Type {
    }

    interface Payload extends SocketEvent.Payload {
        WebSocket getWebSocket();
    }

    final class Container extends SocketEvent.Container {
        private final Type type;

        @Override
        public Type getType() {
            return type;
        }

        public Container(WebSocket webSocket) {
            super(webSocket);
            this.type = new TypeImpl(webSocket);
        }

        private final class TypeImpl extends SocketEvent.Container.TypeImpl implements Type {
            @Override
            public Invocable.TypeMap<? extends Payload> getInstanceSupplier() {
                return StaticCache.access(this, "instanceSupplier",
                        () -> Invocable.<Payload>ofMethodCall(this, "craftOpenPayload").typeMapped());
            }

            public TypeImpl(WebSocket bot) {
                super(Collections.singletonList(getWebSocket().getEventCarrier().Base.getType()), Payload.class, bot);
            }

            public PayloadImpl craftOpenPayload(UniObjectNode data) {
                return new PayloadImpl(this, data);
            }
        }

        private final class PayloadImpl extends SocketEvent.Container.PayloadImpl implements Payload {
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
