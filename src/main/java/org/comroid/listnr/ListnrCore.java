package org.comroid.listnr;

import org.comroid.common.Polyfill;
import org.comroid.common.info.Dependent;
import org.comroid.common.info.ExecutorBound;
import org.comroid.listnr.model.EventPayload;
import org.comroid.listnr.model.EventType;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

public abstract class ListnrCore<IN, D, T extends EventType<IN, D, ? extends P>, P extends EventPayload<D, ? extends T>>
        implements Dependent<D>, ExecutorBound {
    private final Collection<EventType<IN, D, ? extends T>> types = new ArrayList<>();
    private final Map<Listnr.Attachable<IN, D, ? extends T, ? extends P>, EventConsumers> consumers = new ConcurrentHashMap<>();
    private final Class<IN> inClass;
    private final D dependent;
    private final Executor executor;

    @Override
    public Executor getExecutor() {
        return executor;
    }

    public Collection<EventType<IN, D, ? extends T>> getRegisteredEventTypes() {
        return types;
    }

    @Override
    public @Nullable D getDependent() {
        return dependent;
    }

    /**
     * @param inTypeClass Type class to represent the IN parameter.
     * @param dependent The dependency object.
     */
    protected ListnrCore(Class<IN> inTypeClass, D dependent) {
        this(ForkJoinPool.commonPool(), inTypeClass, dependent);
    }

    protected ListnrCore(Executor executor, Class<IN> inTypeClass, D dependent) {
        this.executor = executor;
        this.inClass = inTypeClass;
        this.dependent = dependent;
    }

    public void register(EventType<IN, D, ?> type) {
        types.add(Polyfill.uncheckedCast(type));
    }

    @Internal
    <EP extends P> Runnable listen(final Listnr.Attachable<IN, D, ? extends T, ? extends P> listener,
                                   final EventType<IN, D, ? extends EP> eventType,
                                   final Consumer<EP> payloadConsumer) {
        consumers(listener, eventType).add(payloadConsumer);

        return () -> consumers(listener, eventType).remove(payloadConsumer);
    }

    @Internal
    public <ET extends EventType<IN, D, EventPayload<D, ET>>> void publish(
            final Listnr.Attachable<IN, D, T, P> attachable,
            final ET eventType,
            final Object[] data
    ) {
        final EventPayload<D, ET> payload = eventType.makePayload(Arrays.stream(data)
                .filter(inClass::isInstance)
                .findAny()
                .map(inClass::cast)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("No Input data {type = %s} found in array: %s",
                                inClass.getSimpleName(), Arrays.toString(data))
                )));

        getExecutor().execute(() -> consumers(attachable, Polyfill.uncheckedCast(eventType))
                .forEach(consumer -> consumer.accept(Polyfill.uncheckedCast(payload))));
    }

    private Collection<Consumer<? extends P>> consumers(
            Listnr.Attachable<IN, D, ? extends T, ? extends P> attachable,
            EventType<IN, D, ? extends P> type) {
        return consumers.computeIfAbsent(attachable, EventConsumers::new)
                .computeIfAbsent(type, key -> new ArrayList<>());
    }

    private class EventConsumers extends ConcurrentHashMap<EventType<IN, D, ? extends P>, Collection<Consumer<? extends P>>> {
        @SuppressWarnings("FieldCanBeLocal")
        private final Listnr.Attachable<IN, D, ? extends T, ? extends P> owner;

        public EventConsumers(Listnr.Attachable<IN, D, ? extends T, ? extends P> owner) {
            this.owner = owner;
        }
    }
}
