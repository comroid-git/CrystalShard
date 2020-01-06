package org.comroid.crystalshard.core.cache;

import java.util.Optional;
import java.util.stream.Stream;

import org.comroid.crystalshard.api.entity.Snowflake;
import org.comroid.crystalshard.util.model.NonThrowingCloseable;

public interface Cache<T extends Cacheable> extends NonThrowingCloseable {
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
