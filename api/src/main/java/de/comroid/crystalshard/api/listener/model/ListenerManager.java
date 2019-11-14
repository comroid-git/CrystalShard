package de.comroid.crystalshard.api.listener.model;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.gateway.listener.GatewayListener;
import de.comroid.crystalshard.core.gateway.listener.GatewayListenerManager;
import de.comroid.crystalshard.util.model.Timeoutable;

public interface ListenerManager<L extends Listener> extends Timeoutable {
    Discord getAPI();

    L getListener();

    ListenerManager<L> addDetachHandler(Runnable detachHandler);

    boolean removeDetachHandlerIf(Predicate<Runnable> tester);

    ListenerManager<L> detachNow(boolean fireDetachHandlers);

    ListenerManager<L> detachAfter(int runs);

    boolean isDetached();

    Collection<GatewayListenerManager<? extends GatewayListener>> getGatewayListenerManagers();

    default ListenerManager<L> detachNow() {
        return detachNow(true);
    }

    default ScheduledFuture<?> detachIn(long time, TimeUnit unit) {
        return getAPI()
                .getListenerThreadPool()
                .schedule((Callable<ListenerManager<L>>) this::detachNow, time, unit);
    }

    interface Initializer<T> {
        void initialize(Gateway gateway, T listener);
    }
}
