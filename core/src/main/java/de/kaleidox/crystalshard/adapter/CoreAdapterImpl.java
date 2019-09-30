package de.kaleidox.crystalshard.adapter;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.api.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.api.concurrent.WorkerThread;
import de.kaleidox.crystalshard.core.api.gateway.Gateway;
import de.kaleidox.crystalshard.core.api.rest.DiscordRequest;
import de.kaleidox.crystalshard.core.api.rest.Ratelimiter;
import de.kaleidox.crystalshard.core.api.rest.WebRequest;
import de.kaleidox.crystalshard.core.concurrent.ThreadPoolImpl;
import de.kaleidox.crystalshard.core.concurrent.WorkerThreadImpl;
import de.kaleidox.crystalshard.core.gateway.GatewayImpl;
import de.kaleidox.crystalshard.core.rest.DiscordRequestImpl;
import de.kaleidox.crystalshard.core.rest.RatelimiterImpl;
import de.kaleidox.crystalshard.core.rest.WebRequestImpl;

public final class CoreAdapterImpl extends CoreAdapter {
    public CoreAdapterImpl() {
        mappingTool.implement(WebRequest.class, WebRequestImpl.class)
                .implement(ThreadPool.class, ThreadPoolImpl.class, Discord.class)
                .implement(WorkerThread.class, WorkerThreadImpl.class, Discord.class)
                .implement(Gateway.class, GatewayImpl.class, Discord.class)
                .implement(DiscordRequest.class, DiscordRequestImpl.class, Discord.class)
                .implement(Ratelimiter.class, RatelimiterImpl.class, Discord.class);
    }
}
