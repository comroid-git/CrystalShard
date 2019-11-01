package de.comroid.crystalshard.api.listener.model;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import de.comroid.crystalshard.api.event.model.Event;
import de.comroid.crystalshard.util.model.NStream;
import de.comroid.crystalshard.util.model.Pair;

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

    <FE extends Event> CompletableFuture<EventPair<FE, ListenerManager<Listener<? extends FE>>>> listenOnceTo(Class<FE> forEvent);

    <FE extends Event> NStream<EventPair<FE, ListenerManager<Listener<? extends FE>>>> listenInStream(Class<FE> forEvent);

    final class EventPair<E extends Event, LM extends ListenerManager<Listener<? extends E>>> extends Pair<E, LM> {
        public EventPair(E aValue, LM bValue) {
            super(aValue, bValue);
        }

        public E getEvent() {
            return aValue;
        }

        public LM getManager() {
            return bValue;
        }
    }
}

