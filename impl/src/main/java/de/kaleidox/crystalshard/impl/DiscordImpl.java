package de.kaleidox.crystalshard.impl;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.api.event.DiscordEvent;
import de.kaleidox.crystalshard.api.event.model.Event;
import de.kaleidox.crystalshard.api.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.listener.model.Listener;
import de.kaleidox.crystalshard.api.listener.model.ListenerManager;
import de.kaleidox.crystalshard.api.model.user.Yourself;
import de.kaleidox.crystalshard.core.api.cache.CacheManager;
import de.kaleidox.crystalshard.core.api.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.api.gateway.Gateway;
import de.kaleidox.crystalshard.core.api.rest.Ratelimiter;
import de.kaleidox.crystalshard.util.model.NStream;

public class DiscordImpl implements Discord {
    @Override public ThreadPool getCommonThreadPool() {
        return null;
    }

    @Override public ThreadPool getGatewayThreadPool() {
        return null;
    }

    @Override public ThreadPool getListenerThreadPool() {
        return null;
    }

    @Override public ThreadPool getRatelimiterThreadPool() {
        return null;
    }

    @Override public Ratelimiter getRatelimiter() {
        return null;
    }

    @Override public Gateway getGateway() {
        return null;
    }

    @Override public CacheManager getCacheManager() {
        return null;
    }

    @Override public Yourself getYourself() {
        return null;
    }

    @Override public String getToken() {
        return null;
    }

    @Override
    public <TL extends DiscordAttachableListener<? extends DiscordEvent>> ListenerManager<TL> attachListener(TL listener) {
        return null;
    }

    @Override
    public <TL extends DiscordAttachableListener<? extends DiscordEvent>> boolean detachListener(TL listener) {
        return false;
    }

    @Override
    public Collection<ListenerManager<? extends DiscordAttachableListener<? extends DiscordEvent>>> getAttachedListenerManagers() {
        return null;
    }

    @Override
    public <FE extends Event> CompletableFuture<EventPair<FE, ListenerManager<Listener<? extends FE>>>> listenOnceTo(Class<FE> forEvent) {
        return null;
    }

    @Override
    public <FE extends Event> NStream<EventPair<FE, ListenerManager<Listener<? extends FE>>>> listenInStream(Class<FE> forEvent) {
        return null;
    }
}
