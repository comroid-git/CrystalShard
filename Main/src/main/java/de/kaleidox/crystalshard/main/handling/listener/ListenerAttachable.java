package de.kaleidox.crystalshard.main.handling.listener;

import de.kaleidox.crystalshard.main.handling.listener.channel.ChannelAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.message.MessageAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.ServerAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.crystalshard.main.handling.listener.user.UserAttachableListener;
import de.kaleidox.crystalshard.util.objects.functional.Evaluation;

import java.util.Collection;

/**
 * This interface marks an object as listener attachable for listeners of type T.
 *
 * @param <T> The supertype of the listeners that can be attached to this object.
 * @see DiscordAttachableListener
 * @see ServerAttachableListener
 * @see ChannelAttachableListener
 * @see MessageAttachableListener
 * @see RoleAttachableListener
 * @see UserAttachableListener
 */
public interface ListenerAttachable<T extends Listener> {
    /**
     * Attaches a new Listener of type C to the object.
     *
     * @param listener The listener to attach.
     * @param <C>      Type variable so that C extends T.
     * @return The ListenerManager of the attached listener.
     */
    <C extends T> ListenerManager<C> attachListener(C listener);

    /**
     * Tries to detach the given listener from the object.
     *
     * @param listener The listener to detach.
     * @return An Evaluation object that tells if the detach could be performed.
     */
    Evaluation<Boolean> detachListener(T listener);

    /**
     * Returns a collection of all listeners that are attached to this object.
     *
     * @return A collection with the attached listeners.
     */
    Collection<T> getAttachedListeners();

    /**
     * Returns a collection of all ListenerManagers attached to this object.
     *
     * @return A collection with attached ListenerManagers.
     */
    Collection<ListenerManager<? extends T>> getListenerManagers();

    /**
     * Detached all listeners from the current object.
     */
    default void detachAllListeners() {
        getAttachedListeners().forEach(this::detachListener);
    }
}
