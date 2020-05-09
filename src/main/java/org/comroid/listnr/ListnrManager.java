package org.comroid.listnr;

import org.comroid.common.Polyfill;
import org.comroid.restless.socket.WebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ListnrManager<ET extends EventType<?, ?, ? extends EventPayload<? super ET>>,
        TH extends TypeHandler<? super ET, ? extends EventPayload<? super ET>>> {
    private final List<Runnable> detachHandlers = new ArrayList<>();
    private final ListnrHub<?, ?, ? super ET, ListnrAttachable<?, ?, ? extends ET, ? extends EventPayload<? super ET>>> hub;
    private final ListnrAttachable<?, ?, ET, ? extends EventPayload<? super ET>> owner;
    private final ET eventType;
    private final TH handler;

    public ListnrHub<?, ?, ? super ET, ListnrAttachable<?, ?, ? extends ET, ? extends EventPayload<? super ET>>> getHub() {
        return hub;
    }

    public ListnrAttachable<?, ?, ET, ? extends EventPayload<? super ET>> getOwner() {
        return owner;
    }

    public TH getTypeHandler() {
        return handler;
    }

    public ET getEventSuperType() {
        return eventType;
    }

    ListnrManager(
            ListnrHub<?, ?, ? super ET, ListnrAttachable<?, ?, ? extends ET, ? extends EventPayload<? super ET>>> hub,
            ListnrAttachable<?, ?, ET, ? extends EventPayload<? super ET>> owner,
            TH handler
    ) {
        this.hub = hub;
        this.owner = owner;
        this.handler = handler;

        //noinspection unchecked
        this.eventType = (ET) handler.getMasterEventType();
    }

    public void addDetachHandler(Runnable task) {
        detachHandlers.add(task);
    }

    public void detachNow() {
        getOwner().detach(Polyfill.uncheckedCast(this));
        detachHandlers.forEach(Runnable::run);
    }

    public ScheduledFuture<?> detachIn(long time, TimeUnit unit) {
        return getHub().getThreadPool().schedule(this::detachNow, time, unit);
    }
}
