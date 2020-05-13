package org.comroid.restless.socket.event;

import org.comroid.common.Polyfill;
import org.comroid.common.func.Invocable;
import org.comroid.common.ref.StaticCache;
import org.comroid.restless.socket.WebSocket;
import org.comroid.uniform.node.UniObjectNode;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public interface PongEvent {
    interface Type extends SocketEvent.Type<Payload> {
    }

    interface Payload extends SocketEvent.Payload<Type> {
        int getCloseCode();
    }

    final class Container extends SocketEvent.Container<Payload> {
        private final Type type;

        @Override
        public Type getType() {
            return type;
        }

        public Container(WebSocket webSocket) {
            super(webSocket, new CompletableFuture<>());
            this.type = new TypeImpl(webSocket);
            typeFuture.complete(type);
        }

        private final class TypeImpl extends SocketEvent.Container<Payload>.TypeImpl implements Type {
            @Override
            public Invocable.TypeMap<? extends Payload> getInstanceSupplier() {
                return StaticCache.access(this, "instanceSupplier",
                        () -> Invocable.<Payload>ofMethodCall(this, "craftDataPayload").typeMapped());
            }

            public TypeImpl(WebSocket bot) {
                super(Polyfill.uncheckedCast(Collections.singletonList(getWebSocket().getEventHub().Base.getType())), Payload.class, bot);
            }

            public Container.PayloadImpl craftOpenPayload(UniObjectNode data, int closeCode) {
                return new Container.PayloadImpl(this, closeCode);
            }
        }

        private final class PayloadImpl extends SocketEvent.Container<Payload>.PayloadImpl<Type> implements Payload {
            private final int closeCode;

            @Override
            public int getCloseCode() {
                return closeCode;
            }

            public PayloadImpl(Type masterEventType, int closeCode) {
                super(masterEventType, null);

                this.closeCode = closeCode;
            }
        }
    }
}
