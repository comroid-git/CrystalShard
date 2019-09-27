package de.kaleidox.crystalshard.api.listener.model;

import java.util.Collection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListener;
import de.kaleidox.crystalshard.core.api.gateway.listener.GatewayListenerManager;
import de.kaleidox.crystalshard.util.model.Timeoutable;

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
                .getScheduler()
                .schedule(() -> detachNow(), time, unit);
    }
}
