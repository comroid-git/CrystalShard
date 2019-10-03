package de.kaleidox.crystalshard.adapter;

import java.util.function.BiFunction;
import java.util.function.Function;

import de.kaleidox.crystalshard.abstraction.serialization.JsonTraitImpl;
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
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

public final class CoreAdapterImpl extends CoreAdapter {
    public CoreAdapterImpl() throws NoSuchMethodException {
        mappingTool.implement(WebRequest.class, WebRequestImpl.class.getConstructor())
                .implement(ThreadPool.class, ThreadPoolImpl.class.getConstructor(Discord.class))
                .implement(WorkerThread.class, WorkerThreadImpl.class.getConstructor(Discord.class))
                .implement(Gateway.class, GatewayImpl.class.getConstructor(Discord.class))
                .implement(DiscordRequest.class, DiscordRequestImpl.class.getConstructor(Discord.class))
                .implement(Ratelimiter.class, RatelimiterImpl.class.getConstructor(Discord.class))
                .implement(JsonTrait.class, JsonTraitImpl.SimpleJsonTrait.class.getConstructor(Function.class, String.class, Function.class))
                .implement(JsonTrait.class, JsonTraitImpl.ComplexJsonTrait.class.getConstructor(Function.class, String.class, BiFunction.class))
                .implement(JsonTrait.class, JsonTraitImpl.CollectiveJsonTrait.class.getConstructor(String.class, Class.class));
    }
}
