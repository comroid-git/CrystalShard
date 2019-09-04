package de.kaleidox.crystalshard.api.listener;

import java.util.Collection;

public interface ListenerAttachable<AL extends AttachableListener & Listener> {
    <TL extends AL> ListenerManager<TL> attachListener(TL listener);

    <TL extends AL> boolean detachListener(TL listener);

    Collection<ListenerManager<? extends AL>> getAttachedListenerManagers();

    default <TL extends AL> boolean detachListener(ListenerManager<TL> listenerManager) {
        return detachListener(listenerManager.getListener());
    }

    default int detachAllListeners() {
        int c = 0;

        for (ListenerManager<? extends AL> attachedListenerManager : getAttachedListenerManagers())
            if (detachListener(attachedListenerManager))
                c++;

        return c;
    }
}

