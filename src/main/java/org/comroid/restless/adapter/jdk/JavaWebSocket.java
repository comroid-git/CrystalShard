package org.comroid.restless.adapter.jdk;

import org.comroid.restless.socket.WebSocket;

import java.net.URI;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.IntFunction;

public class JavaWebSocket extends WebSocket {
    private final JListener jListener;

    protected JavaWebSocket(HttpClient httpClient, WebSocket.Header.List headers, ThreadGroup threadGroup, URI uri) {
        super(threadGroup);
        this.jListener = new JListener();

        final java.net.http.WebSocket.Builder builder = httpClient.newWebSocketBuilder();

        headers.forEach(head -> builder.header(head.getName(), head.getValue()));

        builder.buildAsync(uri, this.jListener)

    }

    @Override
    protected CompletableFuture<Void> sendString(String data, boolean last) {
        return null;
    }

    private class JListener implements java.net.http.WebSocket.Listener {
        @Override
        public void onOpen(java.net.http.WebSocket webSocket) {

        }

        @Override
        public CompletionStage<?> onText(java.net.http.WebSocket webSocket, CharSequence data, boolean last) {
            return null;
        }

        @Override
        public CompletionStage<?> onBinary(java.net.http.WebSocket webSocket, ByteBuffer data, boolean last) {
            return null;
        }

        @Override
        public CompletionStage<?> onPing(java.net.http.WebSocket webSocket, ByteBuffer message) {
            return null;
        }

        @Override
        public CompletionStage<?> onPong(java.net.http.WebSocket webSocket, ByteBuffer message) {
            return null;
        }

        @Override
        public CompletionStage<?> onClose(java.net.http.WebSocket webSocket, int statusCode, String reason) {
            return null;
        }

        @Override
        public void onError(java.net.http.WebSocket webSocket, Throwable error) {

        }
    }
}
