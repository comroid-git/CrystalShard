package de.comroid.crystalshard.adapter;

import java.lang.reflect.Proxy;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.comroid.crystalshard.abstraction.serialization.JsonBindings;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.core.concurrent.ThreadPool;
import de.comroid.crystalshard.core.concurrent.WorkerThread;
import de.comroid.crystalshard.core.gateway.Gateway;
import de.comroid.crystalshard.core.rest.DiscordRequest;
import de.comroid.crystalshard.core.rest.Ratelimiter;
import de.comroid.crystalshard.core.rest.WebRequest;
import de.comroid.crystalshard.core.concurrent.ThreadPoolImpl;
import de.comroid.crystalshard.core.concurrent.WorkerThreadImpl;
import de.comroid.crystalshard.core.gateway.GatewayImpl;
import de.comroid.crystalshard.core.rest.DiscordRequestImpl;
import de.comroid.crystalshard.core.rest.RatelimiterImpl;
import de.comroid.crystalshard.core.rest.WebRequestImpl;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;

public final class CoreAdapterImpl extends CoreAdapter {
    public CoreAdapterImpl() throws NoSuchMethodException {
        mappingTool.implement(WebRequest.class, WebRequestImpl.class.getConstructor())
                .implement(ThreadPool.class, ThreadPoolImpl.class.getConstructor(Discord.class, String.class, int.class))
                .implement(WorkerThread.class, WorkerThreadImpl.class.getConstructor(Discord.class, ThreadPoolImpl.class, int.class))
                .implement(Gateway.class, GatewayImpl.class.getConstructor(Discord.class, ThreadPool.class))
                .implement(DiscordRequest.class, DiscordRequestImpl.class.getConstructor(Discord.class))
                .implement(Ratelimiter.class, RatelimiterImpl.class.getConstructor(Discord.class))
                .implement(JSONBinding.OneStage.class, JsonBindings.OneStageImpl$Identity.class.getConstructor(String.class, BiFunction.class))
                .implement(JSONBinding.TwoStage.class, JsonBindings.TwoStageImpl$Simple.class.getConstructor(String.class, BiFunction.class, Function.class))
                .implement(JSONBinding.TriStage.class, JsonBindings.TriStageImpl$UnderlyingMapped.class.getConstructor(String.class, BiFunction.class, BiFunction.class));
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
