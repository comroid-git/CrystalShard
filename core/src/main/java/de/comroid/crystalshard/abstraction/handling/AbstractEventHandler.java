package de.comroid.crystalshard.abstraction.handling;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;

import de.comroid.crystalshard.CrystalShard;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.EventBase;
import de.comroid.crystalshard.api.event.EventHandler;
import de.comroid.crystalshard.api.exception.ListenerTimeoutException;
import de.comroid.crystalshard.api.model.ApiBound;
import de.comroid.crystalshard.core.concurrent.ThreadPool;
import de.comroid.crystalshard.util.model.NonThrowingCloseable;
import de.comroid.crystalshard.util.model.Pair;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.Util.hackCast;

public abstract class AbstractEventHandler<E extends EventBase> implements EventHandler<E> {
    private final static FluentLogger logger = FluentLogger.forEnclosingClass();

    private final Map<Class<? extends EventBase>, Collection<HandlerPair>> handlers = new ConcurrentHashMap<>();

    public AbstractEventHandler() {
    }

    @Override
    public <X extends E> EventHandler.API<X> listenTo(Class<X> eventType) {
        return new API<>(eventType);
    }

    @Override
    public <X extends E> NonThrowingCloseable listenUsing(EventAdapter<X> eEventAdapter) {
        class Local implements Consumer<X>, NonThrowingCloseable {
            private final EventAdapter<X> adapter = eEventAdapter;
            final Collection<NonThrowingCloseable> closeables = new ArrayList<>(eEventAdapter.targetTypes.size());

            @Override
            public void accept(X event) {
                adapter.handle(event);
            }

            @Override
            public void close() {
                closeables.forEach(NonThrowingCloseable::close);
            }
        }

        final Local local = new Local();

        for (Class<X> targetType : eEventAdapter.targetTypes)
            local.closeables.add(putHandler(targetType, local));

        return local;
    }

    @Override
    public boolean detachHandlerIf(Class<? extends E> targetType, Predicate<Consumer<E>> filter) {
        return false; // todo
    }

    @Override
    public boolean detachAdapterIf(Class<? extends E> targetType, Predicate<EventAdapter<E>> filter) {
        return false; // todo
    }

    @Internal
    @Override
    public void submitEvent(E event) {
        @SuppressWarnings("unchecked") final Class<? extends EventBase> eventClass =
                (Class<? extends EventBase>) Adapter.getApiClass(event.getClass()).orElseThrow();

        handlers.forEach((targetType, handlers) -> {
            if (targetType.isAssignableFrom(eventClass)) {
                ThreadPool threadPool = api().getListenerThreadPool();

                for (HandlerPair pair : handlers) {
                    Runnable runnable = new Runnable() {
                        private final E myEvent = event;

                        @Override
                        public void run() {
                            try {
                                pair.getAValue().accept(hackCast(myEvent));
                            } catch (Throwable t) {
                                logger.at(Level.SEVERE)
                                        .withCause(t)
                                        .log("Exception in Listener Thread!");
                            }
                        }
                    };

                    threadPool.execute(runnable);
                }
            }
        });
    }

    private Discord api() {
        if (this instanceof ApiBound)
            return ((ApiBound) this).getAPI();

        throw new AssertionError(CrystalShard.PLEASE_REPORT);
    }

    private NonThrowingCloseable putHandler(Class<? extends E> type, Consumer<? extends E> execution) {
        AtomicReference<NonThrowingCloseable> closeable = new AtomicReference<>(null);

        handlers.compute(type, new BiFunction<>() {
            private final Class<? extends E> targetType = type;
            private final AtomicReference<HandlerPair> myself = new AtomicReference<>(null);

            @Override
            public Collection<HandlerPair> apply(Class<? extends EventBase> aClass, Collection<HandlerPair> handlerPairs) {
                if (handlerPairs == null)
                    handlerPairs = new ArrayList<>();

                final HandlerPair pair = new HandlerPair(execution, () -> handlers.get(targetType).remove(myself.get()));

                myself.set(pair);
                closeable.set(pair.getBValue());
                handlerPairs.add(pair);

                return handlerPairs;
            }
        });

        return Objects.requireNonNull(closeable.get(), CrystalShard.PLEASE_REPORT);
    }

    public class API<X extends E> implements EventHandler.API<X> {
        private final Class<X> eventType;

        private boolean concluded = false;

        private Predicate<X> filter = any -> true;
        private @Nullable Duration timeout = null;
        private long counterMax = Long.MAX_VALUE;

        public API(Class<X> eventType) {
            this.eventType = eventType;
        }

        @Override
        public EventHandler.API<X> when(Predicate<X> filter) {
            if (concluded)
                throw new IllegalStateException("API has concluded!");

            this.filter = filter;

            return this;
        }

        @Override
        public EventHandler.API<X> onlyFor(long time, TimeUnit unit) {
            if (concluded)
                throw new IllegalStateException("API has concluded!");

            this.timeout = Duration.ofMillis(unit.toMillis(time));

            return this;
        }

        @Override
        public EventHandler.API<X> onlyFor(long times) {
            if (concluded)
                throw new IllegalStateException("API has concluded!");

            this.counterMax = times;

            return this;
        }

        @Override
        public CompletableFuture<X> onlyOnce() {
            if (concluded)
                throw new IllegalStateException("API has concluded!");

            class Local extends CompletableFuture<X> implements Runnable, Consumer<X> {
                private final AtomicReference<NonThrowingCloseable> detacher = new AtomicReference<>(null);
                private final AtomicBoolean isCancelled = new AtomicBoolean(false);

                {
                    whenComplete((x, t) -> detacher.get().close());
                }

                @Override
                public void accept(X event) {
                    if (isCancelled.get())
                        throw new IllegalStateException("Listener was cancelled before!" + CrystalShard.PLEASE_REPORT);

                    complete(event);
                }

                @Override
                public void run() {
                    if (isDone())
                        return;

                    isCancelled.set(true);
                    completeExceptionally(new ListenerTimeoutException());
                }
            }

            final Local local = new Local();

            if (timeout != null) {
                api().getListenerThreadPool()
                        .schedule(local, timeout.get(ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);
            }

            local.detacher.set(putHandler(eventType, local));
            concluded = true;

            return local;
        }

        @Override
        public NonThrowingCloseable handle(Consumer<X> handler) {
            if (concluded)
                throw new IllegalStateException("API has concluded!");

            class Local implements Consumer<X>, Runnable {
                private final AtomicReference<NonThrowingCloseable> detacher = new AtomicReference<>(null);
                private final Consumer<X> consumer = handler;
                private final Predicate<X> tester = filter;
                private final long maxRuns = counterMax;

                private long counter = 0;

                @Override
                public void accept(X event) {
                    if (tester.test(event)) {
                        consumer.accept(event);
                        counter++;
                    }

                    if (counter > maxRuns)
                        detacher.get().close();
                }

                @Override
                public void run() {
                    detacher.get().close();
                }
            }

            final Local local = new Local();
            local.detacher.set(putHandler(eventType, local));

            if (timeout != null) {
                api().getListenerThreadPool()
                        .schedule(local, timeout.get(ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);
            }

            concluded = true;
            
            return local.detacher.get();
        }
    }

    private final class HandlerPair extends Pair<Consumer<? extends E>, NonThrowingCloseable> {
        public HandlerPair(Consumer<? extends E> aValue, NonThrowingCloseable bValue) {
            super(aValue, bValue);
        }
    }

    private static final class HandlerEntry<E extends EventBase> implements Predicate<Class<? extends E>>, Consumer<E> {
        private final Class<E> type;
        private final Consumer<E> executor;

        private HandlerEntry(Class<E> type, Consumer<E> executor) {
            this.type = type;
            this.executor = executor;
        }

        @Override
        public void accept(E o) {
            executor.accept(o);
        }

        @Override
        public boolean test(Class<? extends E> other) {
            return type.isAssignableFrom(other);
        }
    }
}
