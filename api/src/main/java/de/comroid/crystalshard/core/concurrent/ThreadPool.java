package de.comroid.crystalshard.core.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import de.comroid.crystalshard.api.Discord;

public interface ThreadPool extends ExecutorService, ScheduledExecutorService, ThreadFactory {
    ScheduledExecutorService getUnderlyingScheduler();
    
    static Discord getContextApi() throws IllegalCallerException {
        final Thread thread = Thread.currentThread();

        if (thread instanceof WorkerThread)
            return ((WorkerThread) thread).getAPI();

        throw new IllegalCallerException("ThreadPool.getContextApi() can only be called within CrystalShard Threads!");
    }
}
