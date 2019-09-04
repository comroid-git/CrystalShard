package de.kaleidox.crystalshard.api.listener;

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

    int removeDetachHandlerIf(Predicate<? extends Runnable> tester);

    ListenerManager<L> detachNow();

    ListenerManager<L> detachAfter(int runs);

    Collection<GatewayListenerManager<? extends GatewayListener>> getGatewayListenerManagers();

    default ScheduledFuture<?> detachIn(long time, TimeUnit unit) {
        return getAPI()
                .getThreadPool()
                .getScheduler()
                .schedule(this::detachNow, time, unit);
    }
}
