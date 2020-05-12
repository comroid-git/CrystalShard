package org.comroid.listnr;

import org.comroid.common.Polyfill;
import org.comroid.common.iter.pipe.Pipeable;
import org.comroid.common.iter.pipe.Pump;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public @interface Listnr {
    interface Attachable<IN, D, T extends EventType<IN, D, ? extends P>, P extends EventPayload<D, ? extends T>> {
        ListnrCore<IN, D, T, P> getListnrCore();

        default <ET extends EventType<IN, D, EP>, EP extends EventPayload<D, ET>> Listnr.API<IN, D, ET, EP> listenTo(ET eventType) {
            if (!getListnrCore().getRegisteredEventTypes().contains(eventType))
                throw new IllegalArgumentException(String.format("Type %s is not managed by %s", eventType, this));

            return new Listnr.API<>(Polyfill.uncheckedCast(this), eventType);
        }
    }

    final class API<IN, D, T extends EventType<IN, D, P>, P extends EventPayload<D, T>> implements Pipeable<P> {
        private final Attachable<IN, D, T, P> attachable;
        private final T eventType;

        private API(Attachable<IN, D, T, P> attachable, T eventType) {
            this.attachable = attachable;
            this.eventType = eventType;
        }

        /**
         * @param payloadConsumer The handler for the incoming payloads.
         * @return A runnable that will detach the handler.
         */
        public final Runnable directly(Consumer<P> payloadConsumer) {
            return attachable.getListnrCore().listen(attachable, eventType, payloadConsumer);
        }

        @Override
        public final Pump<?, P> pipe() {
            final Pump<P, P> pump = Pump.create();
            final Runnable detacher = attachable.getListnrCore().listen(attachable, eventType, pump);
            pump.addChildren(detacher::run);

            return pump;
        }

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
            final Runnable detacher = attachable.getListnrCore().listen(attachable, eventType, completer);
            completer.future.thenRunAsync(detacher, Runnable::run);

            return completer.future;
        }
    }
}
