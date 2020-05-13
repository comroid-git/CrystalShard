package org.comroid.listnr;

import org.comroid.common.Polyfill;
import org.comroid.common.iter.pipe.Pipeable;
import org.comroid.common.iter.pipe.Pump;
import org.comroid.listnr.model.EventPayload;
import org.comroid.listnr.model.EventType;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public @interface Listnr {
    interface Attachable<IN, D, T extends EventType<IN, D, ? extends P>, P extends EventPayload<D, ? extends T>> {
        ListnrCore<IN, D, T, P> getListnrCore();

        default <ET extends EventType<IN, D, ? extends EP>, EP extends EventPayload<D, ? extends ET>>
        Listnr.API<IN, D, ET, EP> listenTo(ET eventType) throws IllegalArgumentException {
            verifyEventType(eventType);

            return new Listnr.API<>(Polyfill.uncheckedCast(this), eventType);
        }

        default void publish(EventType<IN, D, ? extends P> eventType, Object... data) {
            verifyEventType(eventType);

            getListnrCore().publish(this, Polyfill.uncheckedCast(eventType), data);
        }

        @Internal
        default <ET extends EventType<IN, D, ? extends EP>, EP extends EventPayload<D, ? extends ET>> void verifyEventType(ET eventType) {
            if (!getListnrCore().getRegisteredEventTypes().contains(eventType))
                throw new IllegalArgumentException(String.format("Type %s is not managed by %s", eventType, this));
        }
    }

    final class API<IN, D, T extends EventType<IN, D, ? extends P>, P extends EventPayload<D, ? extends T>> implements Pipeable<P> {
        private final Attachable<IN, D, T, P> attachable;
        private final T eventType;

        private API(Attachable<IN, D, T, P> attachable, T eventType) {
            this.attachable = attachable;
            this.eventType = eventType;
        }

        /**
         * Listens directly to published data.
         *
         * @param payloadConsumer The handler for the incoming payloads.
         * @return A runnable that will detach the handler.
         */
        public final Runnable directly(Consumer<P> payloadConsumer) {
            return attachable.getListnrCore().listen(attachable, eventType, payloadConsumer);
        }

        @Override
        public Pump<?, P> pipe() {
            return pump();
        }

        /**
         * Listens to data and publishes all of it to a {@link Pump}
         * <p>
         * The returned {@linkplain org.comroid.common.func.Disposable Pump} can be
         * {@linkplain AutoCloseable#close() closed} in order to detach it from this ListnrAttachable
         *
         * @return A pump that will be filled with payloads
         */
        @Override
        public final Pump<?, P> pump() {
            final Pump<P, P> pump = Pump.create();
            final Runnable detacher = attachable.getListnrCore().listen(attachable, eventType, pump);
            pump.addChildren(detacher::run);

            return pump;
        }

        /**
         * Listens to data once and then detaches the consumer
         *
         * @return A future to contain the first received data
         */
        public final CompletableFuture<P> once() {
            class FutureCompleter implements Consumer<P> {
                private final CompletableFuture<P> future = new CompletableFuture<>();

                @Override
                public synchronized void accept(P p) {
                    if (future.isDone())
                        throw new IllegalStateException("Future is already completed");

                    future.complete(p);
                }
            }

            final FutureCompleter completer = new FutureCompleter();
            final Runnable detacher = attachable.getListnrCore()
                    .listen(attachable, eventType, completer);
            completer.future.thenRunAsync(detacher, Runnable::run)
                    .exceptionally(Polyfill.exceptionLogger());

            return completer.future;
        }
    }
}
