package org.comroid.listnr;

import org.comroid.common.Polyfill;
import org.comroid.common.info.Dependent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ListnrCore<IN, D, ID, T extends EventType<IN, D, ? extends EventPayload<D, ? extends T>>>
        implements Dependent<D> {
    private final Collection<EventType<IN, D, ? extends T>> types = new ArrayList<>();
    private final D dependent;

    @Override
    public @Nullable D getDependent() {
        return dependent;
    }

    protected ListnrCore(D dependent) {
        this.dependent = dependent;
    }

    void register(EventType<IN, D, ?> type) {
        types.add(Polyfill.uncheckedCast(type));
    }
}
