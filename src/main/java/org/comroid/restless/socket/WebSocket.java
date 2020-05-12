package org.comroid.restless.socket;

import org.comroid.common.Polyfill;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;

public abstract class WebSocket {
    private final ThreadGroup threadGroup;
    private final WebSocketEventHub webSocketEventHub;
    private IntFunction<String> closeCodeResolver = String::valueOf;

    public final WebSocketEventHub getWebSocketEventHub() {
        return webSocketEventHub;
    }

    public IntFunction<String> getCloseCodeResolver() {
        return closeCodeResolver;
    }

    public void setCloseCodeResolver(IntFunction<String> closeCodeResolver) {
        this.closeCodeResolver = closeCodeResolver;
    }

    protected WebSocket(ThreadGroup threadGroup) {
        this.threadGroup = new ThreadGroup(threadGroup, "websocket");
        this.webSocketEventHub = new WebSocketEventHub(this);
    }

    public final CompletableFuture<Void> sendData(UniNode data) {
        final String string = data.toString();

        if (string.length() < 2048) {
            return sendString(string, true);
        } else {
            final List<String> substrings = new ArrayList<>();

            for (int i = 0; i < (string.length() / 2048) + 1; i++) {
                substrings.add(string.substring(i * 2048, (i + 1) * 2048));
            }

            final CompletableFuture<Void>[] futures = Polyfill.uncheckedCast(new CompletableFuture[substrings.size()]);

            for (int i = 0; i < futures.length; i++) {
                futures[i] = sendString(substrings.get(i), i + 1 >= futures.length);
            }

            return CompletableFuture.allOf(futures);
        }
    }

    protected final void handleData(UniObjectNode data) {

    }

    protected abstract CompletableFuture<Void> sendString(String data, boolean last);

    public static final class Header {
        private final String name;
        private final String value;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public Header(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public static final class List extends ArrayList<Header> {
            public List add(String name, String value) {
                super.add(new Header(name, value));
                return this;
            }
        }
    }
}
