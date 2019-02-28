package de.kaleidox.crystalshard.api.handling.listener;

import de.kaleidox.crystalshard.api.Discord;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface ListenerManager<T extends Listener> {
    Discord getDiscord();

    T getListener();

    ListenerManager<T> detachNow();

    ListenerManager<T> detachIn(long time, TimeUnit unit);

    ListenerManager<T> onDetach(Runnable runnable);

    ListenerManager<T> enable();

    ListenerManager<T> disable();

    boolean isEnabled();
}
