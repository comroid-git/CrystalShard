package org.comroid.restless.socket;

import org.comroid.common.Polyfill;
import org.comroid.common.annotation.Blocking;
import org.comroid.restless.socket.event.PongEvent;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.node.UniNode;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;

public abstract class WebSocket implements WebSocketEventHub.Attachable {
    private final SerializationAdapter<?, ?, ?> seriLib;
    private final ThreadGroup threadGroup;
    private final WebSocketEventHub eventHub;
    private IntFunction<String> closeCodeResolver = String::valueOf;

    public final WebSocketEventHub getEventHub() {
        return eventHub;
    }

    public IntFunction<String> getCloseCodeResolver() {
        return closeCodeResolver;
    }

    public void setCloseCodeResolver(IntFunction<String> closeCodeResolver) {
        this.closeCodeResolver = closeCodeResolver;
    }

    public SerializationAdapter<?, ?, ?> getSerializationAdapter() {
        return seriLib;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    protected WebSocket(SerializationAdapter<?, ?, ?> seriLib, ThreadGroup threadGroup) {
        this.seriLib = seriLib;
        this.threadGroup = new ThreadGroup(threadGroup, "websocket");
        this.eventHub = new WebSocketEventHub(this);
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

    @Blocking
    public final long getPing() {
        final long started = System.currentTimeMillis();
        completePing().join();
        final long finished = System.currentTimeMillis();

        return finished - started;
    }

    public CompletableFuture<PongEvent.Payload> completePing() {
        return sendPing(ByteBuffer.allocate(0))
                .thenCompose(nil -> listenTo(eventHub.Pong.getType()).once());
    }

    protected abstract CompletableFuture<Void> sendString(String data, boolean last);

    protected abstract CompletableFuture<Void> sendPing(ByteBuffer data);

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
