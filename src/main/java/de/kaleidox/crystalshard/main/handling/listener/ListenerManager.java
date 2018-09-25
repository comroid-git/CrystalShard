package de.kaleidox.crystalshard.main.handling.listener;

import de.kaleidox.crystalshard.main.Discord;
import java.util.concurrent.TimeUnit;

public interface ListenerManager<T extends Listener> {
    Discord getDiscord();
    
    T getListener();
    
    ListenerManager<T> detachNow();
    
    ListenerManager<T> detachIn(long time, TimeUnit unit);
    
    ListenerManager<T> onDetach(Runnable runnable);
}
