package de.kaleidox.crystalshard.main.handling.listener;

import de.kaleidox.util.objects.Evaluation;

/**
 * This interface marks an object as listener attachable for listeners of type T.
 *
 * @param <T> The type of the listener.
 */
public interface ListenerAttachable<T extends Listener> {
    <C extends T> ListenerManager<C> attachListener(C listener);

    /**
     * Tries to detach the given listener from the object.
     *
     * @param listener The listener to detach.
     * @return An Evaluation object that tells if the detach could be performed.
     */
    Evaluation<Boolean> detachListener(T listener);
}
