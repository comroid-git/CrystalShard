package org.comroid.listnr;

import org.comroid.common.Polyfill;
import org.comroid.common.info.Dependent;
import org.comroid.common.iter.pipe.Pipe;
import org.comroid.common.iter.pipe.Pipeable;
import org.comroid.common.iter.pipe.Pump;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class ListnrCore<IN, D, T extends EventType<IN, D, ? extends P>, P extends EventPayload<D, ? extends T>>
        implements Dependent<D> {
    private final Collection<EventType<IN, D, ? extends T>> types = new ArrayList<>();
    private final Map<Listnr.Attachable<IN, D, ? extends T, ? extends P>, EventConsumers> consumers = new ConcurrentHashMap<>();
    private final D dependent;

    public Collection<EventType<IN, D, ? extends T>> getRegisteredEventTypes() {
        return types;
    }

    @Override
    public @Nullable D getDependent() {
        return dependent;
    }

    protected ListnrCore(D dependent) {
        this.dependent = dependent;
    }

    @Internal
    <EP extends P> Runnable listen(final Listnr.Attachable<IN, D, ? extends T, ? extends P> listener,
                                   final EventType<IN, D, ? extends EP> eventType,
                                   final Consumer<EP> payloadConsumer) {
        consumers(listener, eventType).add(payloadConsumer);

        return () -> consumers(listener, eventType).remove(payloadConsumer);
    }

    @Internal
    void register(EventType<IN, D, ?> type) {
        types.add(Polyfill.uncheckedCast(type));
    }

    private Collection<Consumer<? extends P>> consumers(
            Listnr.Attachable<IN, D, ? extends T, ? extends P> attachable,
            EventType<IN, D, ? extends P> type) {
        return consumers.computeIfAbsent(attachable, EventConsumers::new)
                .computeIfAbsent(type, key -> new ArrayList<>());
    }

    private class EventConsumers extends ConcurrentHashMap<EventType<IN, D, ? extends P>, Collection<Consumer<? extends P>>> {
        private final Listnr.Attachable<IN, D, ? extends T, ? extends P> owner;

        public EventConsumers(Listnr.Attachable<IN, D, ? extends T, ? extends P> owner) {
            this.owner = owner;
        }
    }
}
