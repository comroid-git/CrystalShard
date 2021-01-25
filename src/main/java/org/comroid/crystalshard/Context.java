package org.comroid.crystalshard;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.entity.SnowflakeCache;
import org.comroid.restless.HttpAdapter;
import org.comroid.restless.REST;
import org.comroid.uniform.SerializationAdapter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

public interface Context extends ContextualProvider.Underlying {
    default SnowflakeCache getCache() {
        return requireFromContext(SnowflakeCache.class);
    }

    default ScheduledExecutorService getExecutor() {
        return requireFromContext(ScheduledExecutorService.class);
    }

    default SerializationAdapter<?,?,?> getSerializer() {
        return requireFromContext(SerializationAdapter.class);
    }


    default HttpAdapter getHttpAdapter() {
        return requireFromContext(HttpAdapter.class);
    }

    default REST getREST() {
        return requireFromContext(REST.class);
    }

    default Bot getBot() {
        return requireFromContext(Bot.class);
    }

    default DiscordBotShard getShard() {
        return requireFromContext(DiscordBotShard.class);
    }

    default <T> Function<Throwable, T> exceptionLogger(
            Logger logger,
            Level level,
            String message
    ) {
        return exceptionLogger(logger, level, message, true);
    }

    default <T> Function<Throwable, T> exceptionLogger(
            Logger logger,
            Level level,
            String message,
            boolean exitWhenHit
    ) {
        return throwable -> {
            logger.log(level, message, throwable);
            if (exitWhenHit)
                System.exit(1);
            return null;
        };
    }
}
