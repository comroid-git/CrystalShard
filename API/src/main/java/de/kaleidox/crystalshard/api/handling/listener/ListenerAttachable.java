package de.kaleidox.crystalshard.api.handling.listener;

import de.kaleidox.crystalshard.api.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.user.UserAttachableListener;
import de.kaleidox.util.functional.Evaluation;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * This interface marks an object as listener attachable for listeners of type T.
 *
 * @param <L> The supertype of the listeners that can be attached to this object.
 * @see DiscordAttachableListener
 * @see ServerAttachableListener
 * @see ChannelAttachableListener
 * @see MessageAttachableListener
 * @see RoleAttachableListener
 * @see UserAttachableListener
 */
public interface ListenerAttachable<L extends Listener> {
    /**
     * Attaches a new Listener of type C to the object.
     *
     * @param listener The listener to attach.
     * @param <C>      Type variable so that C extends T.
     * @return The ListenerManager of the attached listener.
     */
    <C extends L> ListenerManager<C> attachListener(C listener);

    /**
     * Tries to detach the given listener from the object.
     *
     * @param listener The listener to detach.
     * @return An Evaluation object that tells if the detach could be performed.
     */
    Evaluation<Boolean> detachListener(L listener);

    /**
     * Returns a collection of all ListenerManagers attached to this object.
     *
     * @return A collection with attached ListenerManagers.
     */
    default Collection<ListenerManager<? extends L>> getListenerManagers() {
        return getListenerManagers(any -> true);
    }

    /**
     * Returns a collection of all ListenerManagers attached to this object.
     *
     * @return A collection with attached ListenerManagers.
     * @param filter
     */
    Collection<ListenerManager<? extends L>> getListenerManagers(Predicate<ListenerManager<? extends L>> filter);

    /**
     * Returns a collection of all listeners that are attached to this object.
     *
     * @return A collection with the attached listeners.
     */
    default Collection<L> getListeners() {
        return getListeners(any -> true);
    }

    /**
     * Returns a collection of all listeners that are attached to this object.
     *
     * @return A collection with the attached listeners.
     */
    Collection<L> getListeners(Predicate<ListenerManager<? extends L>> filter);

    /**
     * Detached all listeners from the current object.
     */
    default void detachAllListeners() {
        getListeners(any -> true).forEach(this::detachListener);
    }
}
