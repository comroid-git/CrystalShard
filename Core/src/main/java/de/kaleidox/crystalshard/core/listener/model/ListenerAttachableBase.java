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
    protected ConcurrentHashMap<Class, List<ListenerManager<? extends L>>> managers;

    protected ListenerAttachableBase(Discord discord) {
        managers = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends L> ListenerManager<C> attachListener(C listener) {
        Class listenerClass = listener.getClass();
        ListenerManagerImpl<C> listenerManager = managers
                .getOrDefault(listenerClass, new ArrayList<>())
                .stream()
                .filter(manager -> ((ListenerManagerImpl) manager).listener == listener)
                .findAny()
                .map(manager -> (ListenerManagerImpl<C>) manager)
                .orElseGet(() -> new ListenerManagerImpl<>(discord, this, listener));

        managers.putIfAbsent(listenerClass, new ArrayList<>());
        managers.get(listenerClass).add(listenerManager);
        return listenerManager;
    }

    @Override
    public Evaluation<Boolean> detachListener(L listener) {
        Class listenerClass = listener.getClass();
        return managers.entrySet()
                .stream()
                .filter(entry -> entry.getKey() == listenerClass)
                .flatMap(entry -> entry.getValue().stream())
                .map(ListenerManagerImpl.class::cast)
                .filter(manager -> manager.listener == listener)
                .findFirst()
                .map(manager -> Evaluation.of(managers.getOrDefault(listenerClass, new ArrayList<>()).remove(manager)))
                .orElseGet(() -> Evaluation.of(false));
    }

    @Override
    public Collection<ListenerManager<? extends L>> getListenerManagers(
            Predicate<ListenerManager<? extends L>> filter) {
        return managers.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<L> getListeners(Predicate<? super L> filter) {
        return getListenerManagers()
                .stream()
                .map(ListenerManager::getListener)
                .filter(filter)
                .collect(Collectors.toList());
    }
}
