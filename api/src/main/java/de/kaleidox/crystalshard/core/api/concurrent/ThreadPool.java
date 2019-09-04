package de.kaleidox.crystalshard.core.api.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public interface ThreadPool extends ExecutorService, ThreadFactory {
    Executor getExecutor();

    ScheduledExecutorService getScheduler();
}
