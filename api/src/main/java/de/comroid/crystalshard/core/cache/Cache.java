package de.comroid.crystalshard.core.cache;

import java.io.Closeable;
import java.util.Optional;
import java.util.stream.Stream;

import de.comroid.crystalshard.api.entity.Snowflake;

public interface Cache<T extends Cacheable> extends Closeable {
    Optional<T> getByID(long id);

    Optional<T> setToID(long id, T inst);

    <M extends Cacheable> Optional<M> getSingleton(long baseId, Class<M> memberType);

    <M extends Cacheable> Optional<M> setSingleton(long baseId, Class<M> memberType, M inst);

    <M extends Cacheable> Cache<M> getMemberCache(long baseId, Class<M> type);

    Stream<Snowflake> streamSnowflakesByID(long id);

    void delete(long id);

    @Override
    void close();
}
