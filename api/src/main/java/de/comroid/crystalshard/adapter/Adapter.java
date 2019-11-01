package de.comroid.crystalshard.adapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.core.api.rest.DiscordRequest;
import de.comroid.crystalshard.util.Util;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

import com.fasterxml.jackson.databind.JsonNode;
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
        final Class<?> impl = from.getClass();

        return Optional.ofNullable(getApiClass_r(impl));
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
    @Contract("null, null, _ -> null; _, null, _ -> null; _, _, _ -> _")
    public static <R extends Snowflake> R access(
            final Class<? super R> type,
            final Discord api,
            final Object... args
    ) {
        if (type == null) return null;

        final Class[] types = getTypes(args);
        
        if (JsonDeserializable.class.isAssignableFrom(type)) {
            final int locate = Util.arrayLocate(types, JsonNode.class, Class::isAssignableFrom);

            if (locate != -1) {
                final JsonNode node = (JsonNode) args[locate];
                final long id = (long) Snowflake.Trait.ID.extract(node);
                
                return (R) api.getCacheManager()
                        .streamSnowflakesByID(id)
                        .filter(type::isInstance)
                        .findFirst()
                        .map(json -> {
                            json.updateFromJson(node);
                            return json;
                        })
                        .map(type::cast)
                        .orElseGet(() -> create(type, args));
            }
        }
        
        return create(type, args);
    }

    @SuppressWarnings("unchecked")
    @Contract("null, _ -> null; _, _ -> _")
    public static <R> R create(
            final Class<? super R> type,
            final Object... args
    ) {
        if (type == null) return null;

        final Class[] types = getTypes(args);
        
        return implementations.stream()
                .map(adapter -> adapter.mappingTool)
                .map(tool -> tool.find(type, types))
                .findFirst()
                .flatMap(it -> it)
                .map(inst -> inst.apply(args))
                .map(r -> (R) r)
                .orElseThrow(() -> new AssertionError("Class " + type.getName()
                        + " is not instantiable by CrystalShard"));
    }

    @SuppressWarnings("RedundantTypeArguments")
    @Contract("null -> fail")
    public static <R> DiscordRequest<R> request(Discord api) {
        if (api == null) throw new NullPointerException("API is null!");

        return Adapter.<DiscordRequest<R>>create(DiscordRequest.class, api);
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
