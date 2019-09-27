package de.kaleidox.crystalshard.adapter;

import de.kaleidox.crystalshard.core.api.cache.Cache;
import de.kaleidox.crystalshard.core.api.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.api.concurrent.WorkerThread;
import de.kaleidox.crystalshard.core.api.gateway.Gateway;
import de.kaleidox.crystalshard.core.api.rest.DiscordRequest;
import de.kaleidox.crystalshard.core.api.rest.WebRequest;
import de.kaleidox.crystalshard.core.cache.CacheImpl;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.concurrent.WorkerThreadImpl;
import de.kaleidox.crystalshard.core.gateway.GatewayImpl;
import de.kaleidox.crystalshard.core.rest.DiscordRequestImpl;
import de.kaleidox.crystalshard.core.rest.WebRequestImpl;

public final class CoreAdapterImpl extends CoreAdapter {
    @Override
    @SuppressWarnings("unchecked")
    protected <T> Class<? extends T> getImplementingClass(Class<T> of) {
        Class<?> cls = null;

        if (of.isAssignableFrom(WebRequest.class))
            if (of.isAssignableFrom(DiscordRequest.class))
                cls = DiscordRequestImpl.class;
            else cls = WebRequestImpl.class;
        else if (of.isAssignableFrom(Cache.class))
            cls = CacheImpl.class;
        else if (of.isAssignableFrom(ThreadPool.class))
            cls = ThreadPoolImpl.class;
        else if (of.isAssignableFrom(WorkerThread.class))
            cls = WorkerThreadImpl.class;
        else if (of.isAssignableFrom(Gateway.class))
            cls = GatewayImpl.class;

        // todo

        if (cls == null)
            throw new AssertionError("Implementing class not found: " + of);
        else return (Class<? extends T>) cls;
    }
}
