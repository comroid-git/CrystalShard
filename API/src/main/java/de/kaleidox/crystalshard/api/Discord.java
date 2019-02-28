package de.kaleidox.crystalshard.api;

import java.util.Collection;
import java.util.ListIterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.Injector;
import de.kaleidox.crystalshard.api.entity.user.Self;
import de.kaleidox.crystalshard.api.handling.listener.DiscordAttachableListener;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.api.util.ChannelContainer;
import de.kaleidox.crystalshard.api.util.UserContainer;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.ratelimiting.Ratelimiter;
import de.kaleidox.crystalshard.core.net.socket.WebSocketClient;
import de.kaleidox.crystalshard.util.DiscordUtils;
import de.kaleidox.util.functional.Evaluation;

public interface Discord extends
        UserContainer,
        ChannelContainer,
        ListenerAttachable<DiscordAttachableListener> {
    String getToken();

    // Core stuff
    ThreadPool getThreadPool();

    WebSocketClient getWebSocket();

    Ratelimiter getRatelimiter();

    int shardID();

    int shardCount();

    Discord getShard(int shardNumber);

    Self getYourself();

    // AddOn Stuff
    DiscordUtils getUtilities();

    default Executor getExecutor() {
        return getThreadPool().getExecutor();
    }

    default ScheduledExecutorService getScheduler() {
        return getThreadPool().getScheduler();
    }

    static Builder builder() {
        return Injector.create(Discord.Builder.class);
    }

    interface Builder {
        Builder setToken(String token);

        CompletableFuture<Discord> login();
    }

    interface Group extends Discord { // TODO
    }
}
