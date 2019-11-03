package de.comroid.crystalshard.adapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.core.api.cache.Cacheable;
import de.comroid.crystalshard.core.api.rest.DiscordRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.Contract;

public abstract class Adapter {
    private static FluentLogger log = FluentLogger.forEnclosingClass();
    private static Set<Adapter> implementations = new HashSet<>();

    protected ImplementationMapping mappingTool;

    public Adapter() {
        implementations.add(this);

        mappingTool = new ImplementationMapping();
    }
    
    public static Optional<Class<?>> getApiClass(final Object from) {
        if (from instanceof Class)
            return Optional.ofNullable(getApiClass_r((Class<?>) from));

        return Optional.ofNullable(getApiClass_r(from.getClass()));
    }

    private static <T> Class<?> getApiClass_r(Class<?> impl) {
        final Class<?>[] interfaces = impl.getInterfaces();

        for (Class<?> anInterface : interfaces) {
            if (anInterface.isAnnotationPresent(MainAPI.class))
                return anInterface;
            else {
                final Class<?> yield = getApiClass_r(anInterface);
                
                if (yield != null) return yield;
            }
        }
        
        return null;
    }

    @SuppressWarnings("unchecked")
    @Contract("null, _ -> fail; _, _ -> _")
    public static <R> R require(final Class<? super R> type, final Object... param) {
        return ((Cacheable.class.isAssignableFrom(type) && param.length == 2 && (param[0] instanceof Discord & (param[1] instanceof JSON || param[1] instanceof Long)))
                ? Optional.of(param)
                : Optional.<Object[]>empty())
                .flatMap((Function<Object[], Optional<R>>) args -> {
                    long id;

                    if (args[1] instanceof Long)
                        id = (long) args[1];
                    else {
                        if (args[1] instanceof JSONArray)
                            throw new IllegalArgumentException("Cannot instantiate from JSONArray!");
                        id = Snowflake.Trait.ID.extractValue((JSONObject) args[1]);
                    }

                    return ((Discord) args[0]).getCacheManager()
                            .getByID((Class<? extends Snowflake>) getApiClass_r(type), id)
                            .map(it -> (R) it);
                })
                .or(() -> implementations.stream()
                        .map(adapter -> adapter.mappingTool)
                        .map(tool -> tool.find(type, getTypes(param)))
                        .findFirst()
                        .flatMap(it -> it)
                        .map(inst -> inst.apply(param))
                        .map(it -> (R) it))
                .orElseThrow(() -> new AssertionError("Class " + type.getName() + " is not instantiable by CrystalShard"));
    }

    @SuppressWarnings("RedundantTypeArguments")
    @Contract("null -> fail")
    public static <R> DiscordRequest<R> request(Discord api) {
        if (api == null) throw new NullPointerException("API is null!");

        return Adapter.<DiscordRequest<R>>require(DiscordRequest.class, api);
    }

    public static <R, T> R staticOverride(Class<T> type, String method, Object... args) {
        // todo
        return null;
    }

    protected static <T extends Adapter> T loadAdapter(Class<T> adapter) {
        ServiceLoader<T> loader = ServiceLoader.load(adapter);
        Iterator<T> iterator = loader.iterator();

        if (!iterator.hasNext())
            throw new IllegalStateException("No implementation of " + adapter.getSimpleName() + " was found! " +
                    "Are you missing a module?");

        T impl = iterator.next();

        if (iterator.hasNext())
            log.at(Level.FINE).log("Multiple implementations of %s were found!", adapter.getSimpleName());

        return impl;
    }

    static Class[] getTypes(Object... args) {
        Class[] types = new Class[args.length];

        for (int i = 0; i < args.length; i++)
            types[i] = args[i].getClass();

        return types;
    }
}
