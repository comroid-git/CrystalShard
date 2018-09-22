package de.kaleidox.crystalshard.internal.handling;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.handling.listener.Listener;
import de.kaleidox.crystalshard.main.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.main.handling.listener.ListenerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ListenerManagerInternal<T extends Listener> implements ListenerManager<T> {
    private final static ConcurrentHashMap<Integer, ListenerManagerInternal<? extends Listener>> instances       =
            new ConcurrentHashMap<>();
    private final        DiscordInternal                                                         discord;
    private final        T                                                                       listener;
    private final        List<ListenerAttachable<T>>                                             attachedTo      =
            new ArrayList<>();
    private final        List<Runnable>                                                          detachRunnables =
            new ArrayList<>();
    
    ListenerManagerInternal(DiscordInternal discord, T listener) {
        this.discord = discord;
        this.listener = listener;
        instances.putIfAbsent(listener.hashCode(), this);
    }
    
    // Override Methods
    @Override
    public DiscordInternal getDiscord() {
        return discord;
    }
    
    @Override
    public T getListener() {
        return listener;
    }
    
    @Override
    public ListenerManager<T> detachNow() {
        discord.getAllListenerManagers()
                .stream()
                .filter(manager -> listener.getClass()
                        .isAssignableFrom(manager.getListener().getClass()))
                .filter(manager -> listener.equals(manager.getListener()))
                .map(ListenerManagerInternal.class::cast)
                .flatMap(manager -> (Stream<ListenerAttachable<T>>) manager.attachedTo.stream())
                .forEachOrdered(attachable -> attachable.detachListener(listener));
        detachRunnables.forEach(Runnable::run);
        return this;
    }
    
    @Override
    public ListenerManager<T> detachIn(long time, TimeUnit unit) {
        discord.getScheduler().schedule(this::detachNow, time, unit);
        return this;
    }
    
    @Override
    public ListenerManager<T> onDetach(Runnable runnable) {
        detachRunnables.add(runnable);
        return this;
    }
    
    public <C extends ListenerAttachable<T>> void addAttached(C attached) {
        attachedTo.add(attached);
    }
    
// Static members
    // Static membe
    @SuppressWarnings("unchecked")
    public static <T extends Listener> ListenerManagerInternal<T> getInstance(DiscordInternal discordInternal,
                                                                              T listener) {
        if (instances.containsKey(listener.hashCode()))
            return (ListenerManagerInternal<T>) instances.get(listener.hashCode());
        else return new ListenerManagerInternal<>(discordInternal, listener);
    }
}
