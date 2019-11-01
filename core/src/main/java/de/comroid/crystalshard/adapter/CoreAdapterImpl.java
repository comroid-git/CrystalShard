package de.comroid.crystalshard.adapter;

import java.lang.reflect.Proxy;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.comroid.crystalshard.abstraction.serialization.AbstractJsonDeserializable;
import de.comroid.crystalshard.abstraction.serialization.JsonTraitImpl;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.model.channel.ChannelMention;
import de.comroid.crystalshard.core.api.concurrent.ThreadPool;
import de.comroid.crystalshard.core.api.concurrent.WorkerThread;
import de.comroid.crystalshard.core.api.gateway.Gateway;
import de.comroid.crystalshard.core.api.rest.DiscordRequest;
import de.comroid.crystalshard.core.api.rest.Ratelimiter;
import de.comroid.crystalshard.core.api.rest.WebRequest;
import de.comroid.crystalshard.core.concurrent.ThreadPoolImpl;
import de.comroid.crystalshard.core.concurrent.WorkerThreadImpl;
import de.comroid.crystalshard.core.gateway.GatewayImpl;
import de.comroid.crystalshard.core.rest.DiscordRequestImpl;
import de.comroid.crystalshard.core.rest.RatelimiterImpl;
import de.comroid.crystalshard.core.rest.WebRequestImpl;
import de.comroid.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;

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
                .implement(JsonTrait.class, JsonTraitImpl.CollectiveJsonTrait.class.getConstructor(String.class, Class.class, int.class))
                .implement(JsonTrait.class, JsonTraitImpl.UnderlyingObjectJsonTrait.class.getConstructor(String.class, Class.class, BiFunction.class))
                .implement(ChannelMention.class, new Class[]{Discord.class, JsonNode.class}, args -> patchInterface(AbstractJsonDeserializable.class, ChannelMention.class, args));
    }

    @SuppressWarnings("unchecked")
    public <T> T patchInterface(final Class<?> baseClass, final Class<T> interfaceClass, Object... baseArgs) {
        try {
            Class[] types = Adapter.getTypes(baseArgs);
            final Object base = baseClass.getConstructor(types).newInstance(baseArgs);

            return (T) Proxy.newProxyInstance(
                    baseClass.getClassLoader(),
                    new Class[]{interfaceClass},
                    (proxy, method, args) -> baseClass.getMethod(method.getName(), method.getParameterTypes())
                            .invoke(base, args)
            );
        } catch (Throwable any) {
            throw new RuntimeException(any);
        }
    }
}
