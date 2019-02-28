package de.kaleidox.crystalshard.core.listener.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.handling.listener.Listener;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.ListenerManager;
import de.kaleidox.util.functional.Evaluation;

public class ListenerAttachableBase<L extends Listener> implements ListenerAttachable<L> {
    protected Discord discord;
    protected ConcurrentHashMap<Class, List<ListenerManager<? extends L>>> listeners;

    protected ListenerAttachableBase(Discord discord) {
        listeners = new ConcurrentHashMap<>();
    }

    @Override
    public <C extends L> ListenerManager<C> attachListener(C listener) {
        ListenerManagerImpl<C> listenerManager = new ListenerManagerImpl<>(discord, this, listener);
        Class listenerClass = listener.getClass();
        listeners.putIfAbsent(listenerClass, new ArrayList<>());
        listeners.get(listenerClass).add(listenerManager);
        return listenerManager;
    }

    @Override
    public Evaluation<Boolean> detachListener(L listener) {
        Class listenerClass = listener.getClass();
        return listeners.entrySet()
                .stream()
                .filter(entry -> entry.getKey() == listenerClass)
                .flatMap(entry -> entry.getValue().stream())
                .map(ListenerManagerImpl.class::cast)
                .filter(manager -> manager.listener == listener)
                .findFirst()
                .map(manager -> Evaluation.of(listeners.getOrDefault(listenerClass, new ArrayList<>()).remove(manager)))
                .orElseGet(() -> Evaluation.of(false));
    }

    @Override
    public Collection<ListenerManager<? extends L>> getListenerManagers(Predicate<ListenerManager<? extends L>> filter) {
        return listeners.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<L> getListeners(Predicate<ListenerManager<? extends L>> filter) {
        return getListenerManagers(filter)
                .stream()
                .map(ListenerManager::getListener)
                .collect(Collectors.toList());
    }
}
