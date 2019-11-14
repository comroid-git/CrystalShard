package de.comroid.crystalshard.abstraction.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.api.listener.model.AttachableListener;
import de.comroid.crystalshard.api.listener.model.Listener;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import de.comroid.crystalshard.api.listener.model.ListenerManager;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;
import de.comroid.crystalshard.util.Util;

public abstract class AbstractListenerManager<L extends Listener & AttachableListener, E extends Event>
        implements ListenerManager<L>, Consumer<E> {
    protected final Discord api;
    protected final ListenerAttachable<L> attachedTo;
    protected final Class<? extends E> eventClass;
    protected final L listener;

    protected final List<Runnable> detachHandlers;
    protected final Collection<GatewayListenerManager<? extends GatewayListener>> gatewayListenerManagers;

    protected boolean detached;
    protected int maxRuns, runs;

    protected AbstractListenerManager(Discord api, ListenerAttachable<L> attachedTo, Class<? extends E> eventClass, L listener) {
        this.api = api;
        this.attachedTo = attachedTo;
        this.eventClass = eventClass;
        this.listener = listener;

        detachHandlers = new ArrayList<>();
        gatewayListenerManagers = new ArrayList<>();

        detached = false;
        maxRuns = -1;
        runs = 0;
    }

    @Override
    public void accept(E event) {
        if (detached) return;

        runs++;
        listener.onEvent(event);

        if (maxRuns >= runs)
            detachNow();
    }

    @Override
    public Discord getAPI() {
        return api;
    }

    @Override
    public L getListener() {
        return listener;
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
        attachedTo.detachListener(listener);

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
    public Collection<GatewayListenerManager<? extends GatewayListener>> getGatewayListenerManagers() {
        return Collections.unmodifiableCollection(gatewayListenerManagers);
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

    public static <L extends Listener & AttachableListener, E extends Event> void submit(
            Discord api,
            Class<L> listenerClass,
            E event
    ) {
        Util.quickStream(250, event.getAffected())
                .map(attachable -> (ListenerAttachable<?>) attachable)
                .map(ListenerAttachable::getAttachedListenerManagers)
                .flatMap(Collection::stream)
                .filter(manager -> !manager.isDetached())
                .filter(manager -> ((AbstractListenerManager) manager).eventClass.isInstance(event))
                .map(ListenerManager::getListener)
                .map(listenerClass::cast)
                .map((Function<L, Runnable>) listener -> () -> listener.onEvent(event))
                .forEachOrdered(api.getListenerThreadPool()::submit);
    }
}
