package de.comroid.crystalshard.core.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import de.comroid.crystalshard.abstraction.AbstractApiBound;
import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.core.api.cache.Cache;
import de.comroid.crystalshard.core.api.cache.CacheManager;
import de.comroid.crystalshard.core.api.cache.Cacheable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;

import static de.comroid.crystalshard.CrystalShard.ISSUES_URL;

@SuppressWarnings("unchecked")
public class CacheManagerImpl extends AbstractApiBound implements CacheManager {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private static final ObjectMapper mapper = new ObjectMapper();

    private final Map<Class, CacheImpl<?>> caches;

    public CacheManagerImpl(Discord api) {
        super(api);

        caches = new ConcurrentHashMap<>();
    }

    @Override
    public <R extends Cacheable> Optional<R> set(Class<R> type, long id, R instance) {
        if (instance.isSubcacheMember() || instance.isSingletonType())
            throw new IllegalArgumentException(instance + " cannot be stored in a parentcache!");

        return getCache(type).setToID(id, instance);
    }

    @Override
    public <M extends Cacheable, B extends Cacheable> Optional<M> setMember(Class<B> baseType, Class<M> memberType, long baseId, long memberId, M instance) {
        if (!instance.isSubcacheMember())
            throw new IllegalArgumentException(instance + " is not a Member Cacheable type!");

        return getCache(baseType)
                .getMemberCache(baseId, memberType)
                .setToID(memberId, instance);
    }

    @Override
    public <M extends Cacheable, B extends Cacheable> Optional<M> setSingleton(Class<B> baseType, Class<M> memberType, long baseId, M instance) {
        if (!instance.isSingletonType())
            throw new IllegalArgumentException(instance + " is not a Singleton Cacheable type!");

        return getCache(baseType)
                .setSingleton(baseId, memberType, instance);
    }

    @Override
    public <R extends Cacheable> R updateOrCreateAndGet(Class<R> type, long id, JsonNode node) {
        return getCache(type)
                .getByID(id)
                .map(inst -> {
                    inst.update(node);

                    /*
                    we might already be done here, if the object existed before
                    if that is not the case, the following #or block will try to
                    create the object using Adapter.create

                    every object that ever gets cached NEEDS to have a constructor
                    with signature of Object::new(Discord, JsonNode)
                     */

                    return inst;
                })
                .or(() -> {
                    try {
                        return Optional.of(Adapter.create(type, api, node));
                    } catch (Throwable t) { // any throwable, possibly by Adapter.create
                        // unrecoverable situation. developer action needs to be taken if this is reached.
                        throw new RuntimeException("Could not create instance of "
                                + type.toGenericString() + ". Please open an issue at " + ISSUES_URL, t);
                    }
                })
                // this exception will never be thrown, block is required for wrapping the optional properly
                .orElseGet(() -> {
                        /*
                        for an unknown reason, an #orElseThrow call will not recognize a
                        RuntimeException here, so we hack the compiler (again)
                        */
                    throw new AssertionError();
                });
    }

    @Override
    public <M extends Cacheable, B extends Cacheable> M updateOrCreateMemberAndGet(
            Class<B> baseType,
            Class<M> memberType,
            long baseId,
            long memberId,
            JsonNode node
    ) {
        return getCache(baseType)
                .getMemberCache(baseId, memberType)
                .getByID(memberId)
                .map(inst -> {
                    inst.update(node);

                    /*
                    we might already be done here, if the object existed before
                    if that is not the case, the following #or block will try to
                    create the object using Adapter.create

                    every object that ever gets cached NEEDS to have a constructor
                    with signature of Object::new(Discord, JsonNode)
                     */

                    return inst;
                })
                .or(() -> {
                    try {
                        return Optional.of(Adapter.create(memberType, api, node));
                    } catch (Throwable t) { // any throwable, possibly by Adapter.create
                        // unrecoverable situation. developer action needs to be taken if this is reached.
                        throw new RuntimeException("Could not create instance of "
                                + memberType.toGenericString() + ". Please open an issue at " + ISSUES_URL, t);
                    }
                })
                // this exception will never be thrown, block is required for wrapping the optional properly
                .orElseThrow(AssertionError::new);
    }

    @Override
    public <M extends Cacheable, B extends Cacheable> M updateOrCreateSingletonMemberAndGet(
            Class<B> baseType,
            Class<M> memberType,
            long baseId,
            JsonNode node
    ) {
        return getCache(baseType)
                .getSingleton(baseId, memberType)
                .map(inst -> {
                    inst.update(node);

                    /*
                    we might already be done here, if the object existed before
                    if that is not the case, the following #or block will try to
                    create the object using Adapter.create

                    every object that ever gets cached NEEDS to have a constructor
                    with signature of Object::new(Discord, JsonNode)
                     */

                    return inst;
                })
                .or(() -> {
                    try {
                        // all AbstractCacheable
                        return Optional.of(Adapter.create(memberType, api, node));
                    } catch (Throwable t) { // any throwable, possibly by Adapter.create
                        // unrecoverable situation. developer action needs to be taken if this is reached.
                        throw new RuntimeException("Could not create instance of "
                                + memberType.toGenericString() + ". Please open an issue at " + ISSUES_URL, t);
                    }
                })
                // this exception will never be thrown, block is required for wrapping the optional properly
                .orElseThrow(AssertionError::new);
    }

    @Override
    public <R extends Cacheable> Void delete(Class<R> type, long id) {
        getCache(type).delete(id);

        return null;
    }

    @Override
    public <B extends Cacheable, M extends Cacheable> Void deleteMember(Class<B> baseType, Class<M> memberType, long baseId, long memberId) {
        getCache(baseType)
                .getMemberCache(baseId, memberType)
                .delete(memberId);

        return null;
    }

    @Override
    public <R extends Cacheable> Cache<R> getCache(Class<R> forType) {
        return (Cache<R>) caches.compute(getKeyClass(forType),
                (k, v) -> v == null ? new CacheImpl<>(forType, false) : v);
    }

    @Override
    public Stream<Snowflake> streamSnowflakesByID(long id) {
        return caches.values()
                .stream()
                .map(cache -> cache.getSnowflakesByID(id))
                .flatMap(Collection::stream);
    }

    // todo Inspect behavior of this
    static Class<? extends Cacheable> getKeyClass(Class<?> forType) {
        Class<?>[] interfaces = forType.getInterfaces();

        for (Class<?> anInterface : interfaces) {
            if (anInterface.getPackageName().contains("de.comroid.crystalshard.api.")
                    && Arrays.asList(anInterface.getInterfaces()).contains(Cacheable.class)) {
                return (Class<? extends Cacheable>) anInterface;
            }
        }

        for (Class<?> itf : interfaces) {
            Class<? extends Cacheable> aClass = getKeyClass(itf);

            if (aClass != null)
                return aClass;
        }

        return null;
    }
}
