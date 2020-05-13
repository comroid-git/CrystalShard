package org.comroid.restless.adapter.jdk;

import com.google.common.flogger.FluentLogger;
import org.comroid.listnr.ListnrCore;
import org.comroid.restless.socket.WebSocket;
import org.comroid.restless.socket.event.SocketEvent;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.node.UniObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class JavaWebSocket extends WebSocket {
    public static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private final java.net.http.WebSocket jSocket;
    private final JListener jListener;

    @Override
    public ListnrCore<UniObjectNode, WebSocket, SocketEvent.Type<?>, SocketEvent.Payload<?>> getListnrCore() {
        return getEventHub();
    }

    protected JavaWebSocket(SerializationAdapter<?, ?, ?> seriLib, HttpClient httpClient, Header.List headers, ThreadGroup threadGroup, URI uri) {
        super(seriLib, threadGroup);

        java.net.http.WebSocket.Builder builder = httpClient.newWebSocketBuilder();
        headers.forEach(head -> builder.header(head.getName(), head.getValue()));

        this.jListener = new JListener();
        this.jSocket = builder.buildAsync(uri, this.jListener).join();
    }

    @Override
    protected CompletableFuture<Void> sendString(String data, boolean last) {
        return jSocket.sendText(data, last).thenApplyAsync(nil -> null);
    }

    @Override
    protected CompletableFuture<Void> sendPing(ByteBuffer data) {
        return jSocket.sendPing(data).thenApply(nil -> null);
    }

    private class JListener implements java.net.http.WebSocket.Listener {
        private StringBuilder sb = new StringBuilder();

        @Override
        public synchronized void onOpen(java.net.http.WebSocket webSocket) {
            JavaWebSocket.this.publish(getEventHub().Open.getType());

            webSocket.request(1);
        }

        @Override
        public synchronized CompletionStage<?> onText(java.net.http.WebSocket webSocket, CharSequence data, boolean last) {
            sb.append(data);

            if (last) {
                final String str = cleanSB();

                JavaWebSocket.this.publish(getEventHub().Data.getType(), str, getSerializationAdapter().createUniNode(str));
            }

            webSocket.request(1);
            return null;
        }

        @Override
        public synchronized CompletionStage<?> onBinary(java.net.http.WebSocket webSocket, ByteBuffer data, boolean last) {
            sb.append(data.asCharBuffer().toString());

            if (last) {
                final String str = cleanSB();

                JavaWebSocket.this.publish(getEventHub().Data.getType(), str, getSerializationAdapter().createUniNode(str));
            }

            webSocket.request(1);
            return null;
        }

        @Override
        public synchronized CompletionStage<?> onPing(java.net.http.WebSocket webSocket, ByteBuffer message) {
            webSocket.sendPong(message);

            JavaWebSocket.this.publish(getEventHub().Ping.getType(), message);

            webSocket.request(1);
            return null;
        }

        @Override
        public synchronized CompletionStage<?> onPong(java.net.http.WebSocket webSocket, ByteBuffer message) {
            webSocket.sendPing(message);

            JavaWebSocket.this.publish(getEventHub().Pong.getType(), message);

            webSocket.request(1);
            return null;
        }

        @Override
        public synchronized CompletionStage<?> onClose(java.net.http.WebSocket webSocket, int statusCode, String reason) {
            JavaWebSocket.this.publish(getEventHub().Close.getType(), statusCode, reason);

            return null;
        }

        @Override
        public synchronized void onError(java.net.http.WebSocket webSocket, Throwable error) {
            webSocket.sendClose(1000, error.getMessage());
        }

        private String cleanSB() {
            final String bfr = sb.toString();
            sb = new StringBuilder();
            return bfr;
        }
    }
}
