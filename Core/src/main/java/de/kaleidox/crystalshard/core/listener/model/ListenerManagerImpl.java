package de.kaleidox.crystalshard.core.listener.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.handling.listener.Listener;
import de.kaleidox.crystalshard.api.handling.listener.ListenerManager;

public class ListenerManagerImpl<L extends Listener> implements ListenerManager<L> {
    private final Discord discord;
    private final ListenerAttachableBase<? super L> base;
    final L listener;
    boolean enabled;
    private List<Runnable> detachHandlers;

    public ListenerManagerImpl(Discord discord, ListenerAttachableBase<? super L> base, L listener) {
        this.discord = discord;
        this.base = base;
        this.listener = listener;

        enabled = true;
        detachHandlers = new ArrayList<>();
    }

    @Override
    public Discord getDiscord() {
        return discord;
    }

    @Override
    public L getListener() {
        return listener;
    }

    @Override
    public ListenerManager<L> detachNow() {
        base.detachListener(listener);
        detachHandlers.forEach(Runnable::run);
        return this;
    }

    @Override
    public ListenerManager<L> detachIn(long time, TimeUnit unit) {
        discord.getScheduler().schedule(this::detachNow, time, unit);
        return this;
    }

    @Override
    public ListenerManager<L> onDetach(Runnable runnable) {
        detachHandlers.add(runnable);
        return this;
    }

    @Override
    public ListenerManager<L> enable() {
        enabled = true;
        return this;
    }

    @Override
    public ListenerManager<L> disable() {
        enabled = false;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
