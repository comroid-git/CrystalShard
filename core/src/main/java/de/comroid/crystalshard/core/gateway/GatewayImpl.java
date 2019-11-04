package de.comroid.crystalshard.core.gateway;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;

import de.comroid.crystalshard.CrystalShard;
import de.comroid.crystalshard.abstraction.listener.AbstractListenerManager;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.core.api.concurrent.ThreadPool;
import de.comroid.crystalshard.core.api.gateway.Gateway;
import de.comroid.crystalshard.core.api.gateway.GatewayStatusCodes;
import de.comroid.crystalshard.core.api.gateway.OpCode;
import de.comroid.crystalshard.core.api.gateway.event.GatewayEventBase;
import de.comroid.crystalshard.core.api.gateway.event.HELLO;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.api.gateway.listener.GatewayListenerManager;
import de.comroid.crystalshard.core.api.rest.DiscordEndpoint;
import de.comroid.crystalshard.core.api.rest.RestMethod;
import de.comroid.crystalshard.util.annotation.InitializedBy;
import de.comroid.crystalshard.util.model.NStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;

import static de.comroid.crystalshard.CrystalShard.URL;
import static de.comroid.crystalshard.CrystalShard.VERSION;

public class GatewayImpl implements Gateway {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    private final Discord api;
    public final CompletableFuture<Void> helloFuture;
    private final HttpClient client;
    private final SocketListener socketListener;
    private final WebSocket socket;

    private final Collection<BasicGatewayListener<?, ?>> listenerManagers;
    private final ThreadPool threadPool;

    public GatewayImpl(Discord api, ThreadPool threadPool) {
        this.api = api;
        this.threadPool = threadPool;

        // prepare receiving HELLO
        helloFuture = listenOnceTo(HELLO.class)
                .thenAccept(pair -> {
                    final int interval = pair.getEvent().getHeartbeatInterval();

                    threadPool.getScheduler()
                            .scheduleAtFixedRate(() -> this.sendRequest(OpCode.HEARTBEAT, JSON.parseObject("{}")),
                                    interval, interval, TimeUnit.MILLISECONDS);
                });

        try {
            client = HttpClient.newBuilder()
                    .build();
            socketListener = new SocketListener();
            socket = client.newWebSocketBuilder()
                    .header("Authorization", "Bot " + api.getToken())
                    .header("User-Agent", "DiscordBot (" + URL + ", " + VERSION + ") using CrystalShard")
                    .buildAsync(new URI(CrystalShard.GATEWAY_DEFAULT_URL), socketListener)
                    // in case the stored URI does not work, request a new one
                    .exceptionally(throwable -> client.newWebSocketBuilder()
                            .header("Authorization", "Bot " + api.getToken())
                            .header("User-Agent", "DiscordBot (" + URL + ", " + VERSION + ") using CrystalShard")
                            .buildAsync(Adapter.<String>request(api)
                                    .endpoint(DiscordEndpoint.GATEWAY)
                                    .method(RestMethod.GET)
                                    .executeAs(node -> node.getString("url"))
                                    .thenApply(str -> {
                                        try {
                                            return new URI(str);
                                        } catch (URISyntaxException e) {
                                            throw new AssertionError("Unexpected URISyntaxException", e);
                                        }
                                    })
                                    .join(), socketListener)
                            .join())
                    /*
                    ok what the join, but nor is any exception expected on any of
                    these joins, nor should the exceptionally block ever run
                    */
                    .join();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Initialization Exception", e);
        }

        listenerManagers = new ArrayList<>();
    }

    @Override
    public CompletableFuture<Void> sendRequest(OpCode code, JSONObject payload) {
        return CompletableFuture.supplyAsync(() -> {
            JSONObject json = new JSONObject();

            json.put("op", code.value);
            json.put("d", payload);

            return socket.sendText(json.toString(), true);
        }, threadPool).thenApply(nil -> null);
    }

    @Override
    public <TL extends GatewayListener> ListenerManager<TL> attachListener(TL listener) {
        //noinspection unchecked
        return attachListener_impl(listener);
    }

    @Override
    public <TL extends GatewayListener> boolean detachListener(TL listener) {
        return listenerManagers.removeIf(bgl -> bgl.underlyingListener.equals(listener));
    }

    @Override
    public Collection<ListenerManager<? extends GatewayListener>> getAttachedListenerManagers() {
        return null;
    }

    @Override
    public <FE extends Event> CompletableFuture<EventPair<FE, ListenerManager<Listener<? extends FE>>>> listenOnceTo(Class<FE> forEvent) {
        return null; // todo
    }

    @Override
    public <FE extends Event> NStream<EventPair<FE, ListenerManager<Listener<? extends FE>>>> listenInStream(Class<FE> forEvent) {
        return null; // todo
    }

    @Override
    public Discord getAPI() {
        return api;
    }

    private <L extends GatewayListener<E>, E extends GatewayEventBase> ListenerManager<L> attachListener_impl(L listener) {
        BasicGatewayListener<L, E> gatewayListener = new BasicGatewayListener<>(listener);

        listenerManagers.add(gatewayListener);

        return gatewayListener;
    }

    private <L extends GatewayListener<E>, E extends GatewayEventBase> void dispatch(final String jsonStr) {
        log.at(Level.FINER).log("Dispatching data: " + jsonStr);

        threadPool.submit(() -> {
            try {
                final JSONObject json = JSON.parseObject(jsonStr);

                OpCode op = OpCode.getByValue(json.getInteger("op"));
                final JSONObject data = json.getJSONObject("d");
                int seq = json.getInteger("s");
                String type = json.getString("t");

                GatewayEventBase event = null;

                (listenerManagers.size() > 200
                        ? listenerManagers.parallelStream()
                        : listenerManagers.stream())
                        .filter(basicGatewayListener -> basicGatewayListener.test(type))
                        .map((Function<BasicGatewayListener, Runnable>) basicGatewayListener -> {
                            Class<? extends ListenerManager<? extends GatewayListener>> managerClass
                                    = getManagerClass(basicGatewayListener.underlyingListener.getClass());
                            @SuppressWarnings("unchecked")
                            Class<? extends Listener<? extends GatewayEventBase>> declaringClass
                                    = (Class<? extends Listener<? extends GatewayEventBase>>) managerClass.getDeclaringClass();

                            Objects.requireNonNull(declaringClass,
                                    managerClass + " should be declared by its listener class!");

                            EventData<L, E> eventData = new EventData<>(declaringClass, event);

                            //noinspection unchecked
                            return () -> basicGatewayListener.accept(eventData);
                        })
                        .forEachOrdered(api.getListenerThreadPool()::submit);
            } catch (JSONException e) {
                throw new RuntimeException("JSON Deserialization Exception", e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <TL extends AttachableListener & Listener> Class<ListenerManager<TL>> getManagerClass(Class<TL> aClass) {
        InitializedBy initializedBy = aClass.getAnnotation(InitializedBy.class);

        Objects.requireNonNull(initializedBy, "Internal Error: Listener class " + aClass + " does not have a " +
                "@ManagedBy definition. Please open an issue at " + CrystalShard.ISSUES_URL);

        return (Class<ListenerManager<TL>>) initializedBy.value();
    }

    class BasicGatewayListener<L extends GatewayListener<E>, E extends GatewayEventBase>
            implements GatewayListener<EventData<L, E>>, GatewayListenerManager<L>, Predicate<String>, Consumer<EventData<L, E>> {
        private final GatewayListener<E> underlyingListener;

        private final Collection<Runnable> detachHandlers;
        protected int maxRuns, runs;
        private boolean detached;

        BasicGatewayListener(GatewayListener<E> underlyingListener) {
            this.underlyingListener = underlyingListener;

            detachHandlers = new ArrayList<>();

            detached = false;
            maxRuns = -1;
            runs = 0;
        }

        @Override
        public Discord getAPI() {
            return api;
        }

        @Override
        @SuppressWarnings("unchecked")
        public L getListener() {
            return (L) underlyingListener;
        }

        @Override
        public ListenerManager<L> addDetachHandler(Runnable detachHandler) {
            detachHandlers.add(detachHandler);

            return this;
        }

        @Override
        public boolean removeDetachHandlerIf(Predicate<Runnable> tester) {
            return detachHandlers.removeIf(tester);
        }

        @Override
        public ListenerManager<L> detachNow(boolean fireDetachHandlers) {
            detached = true;
            GatewayImpl.this.detachListener((GatewayListener<EventData<L, E>>) this);

            if (fireDetachHandlers)
                detachHandlers.forEach(runnable -> api.getListenerThreadPool()
                        .submit(runnable));

            return this;
        }

        @Override
        public ListenerManager<L> detachAfter(int runs) {
            this.maxRuns = runs;

            return this;
        }

        @Override
        public boolean isDetached() {
            return detached;
        }

        @Override
        public ScheduledFuture<?> timeout(long time, TimeUnit unit, Runnable timeoutHandler) {
            return api.getListenerThreadPool()
                    .getScheduler()
                    .schedule(() -> {
                        detachNow(false);

                        timeoutHandler.run();
                    }, time, unit);
        }

        @Override
        public void onEvent(EventData<L, E> eventData) {
            api.getListenerThreadPool().submit(() -> underlyingListener.onEvent(eventData.event));
            AbstractListenerManager.submit(api, eventData.listenerClass, eventData.event);
        }

        @Override
        public void accept(EventData<L, E> eventData) {
            if (detached) return;

            runs++;
            this.onEvent(eventData);

            if (maxRuns >= runs)
                detachNow();
        }

        @Override
        public boolean test(String actualName) {
            try {
                Class<? extends GatewayListener> underlyingListenerClass = underlyingListener.getClass();

                Field nameField = underlyingListenerClass.getField("NAME");
                String gatewayListenerName = nameField.get(null).toString();

                return actualName.equals(gatewayListenerName);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Reflective Operation Excpetion", e);
            }
        }
    }

    static class EventData<L extends Listener<? extends GatewayEventBase>, E extends GatewayEventBase> implements GatewayEventBase {
        final Class<L> listenerClass;
        final E event;

        @SuppressWarnings("unchecked")
        EventData(Class<? extends Listener<? extends GatewayEventBase>> listenerClass, GatewayEventBase event) {
            this.listenerClass = (Class<L>) listenerClass;
            this.event = (E) event;
        }

        @Override
        public Gateway getGateway() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListenerAttachable[] getAffected() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Discord getAPI() {
            return null; // todo test behavior
        }
    }

    class SocketListener implements WebSocket.Listener {
        StringBuilder strb = new StringBuilder();

        @Override
        public void onOpen(WebSocket webSocket) {
            log.at(Level.INFO).log("WebSocket opened!");

            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            log.at(Level.FINEST).log("Received textual data: " + data);

            strb.append(data);

            if (last) {
                api.getGatewayThreadPool().submit(() -> dispatch(strb.toString()));

                strb = new StringBuilder();
            }

            webSocket.request(1);
            return null;
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            log.at(Level.INFO).log("WebSocket closed with status " + GatewayStatusCodes.toString(statusCode));

            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            log.at(Level.SEVERE).withCause(error).log("WebSocket encountered an Error");
        }
    }
}
