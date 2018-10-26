package de.kaleidox.crystalshard.core.concurrent;

import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.IllegalThreadException;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public interface ThreadPool {
    Supplier<IllegalThreadException> BOT_THREAD_EXCEPTION = () -> new IllegalThreadException(
            "That method may only be called from a bot-own " + "thread, such as in listeners or scheduler tasks. You may not use it " + "from contexts like " +
            "CompletableFuture#thenAcceptAsync or such. User " + "ThreadPool#isBotOwnThread to check if the current " + "Thread belongs to the Bot.");
    
    /**
     * Schedules a new task. If there is no limit on the ThreadPool or the limit is not hit, ensures that there is a Worker available.
     *
     * @param task        The task to execute.
     * @param description A short description on what this task does.
     */
    void execute(Runnable task, String... description);
    
    /**
     * Gets the Scheduler for this ThreadPool. This will be useful for e.g. updating the discord status.
     *
     * @return The ScheduledExecutorService for this ThreadPool.
     */
    ScheduledExecutorService getScheduler();
    
    /**
     * Returns whether the current thread is a BotOwn thread; meaning the current thread is a {@link Worker} Thread. This method is required if you statically
     * want to get a Discord instance using {@link #getThreadDiscord()}.
     *
     * @return Whether the current thread is a bot own thread.
     */
    static boolean isBotOwnThread() {
        return Thread.currentThread() instanceof Worker;
    }
    
    /**
     * Checks if the current thread is a BotOwn thread (see {@link #isBotOwnThread()}, and if so, returns the {@link Worker} Thread.
     *
     * @return The worker thread.
     * @throws IllegalThreadException If the thread is not a Bot-Own thread.
     */
    static Worker requireBotOwnThread() {
        Thread thread = Thread.currentThread();
        if (thread instanceof Worker) {
            return (Worker) thread;
        } else throw new IllegalThreadException();
    }
    
    /**
     * Checks if the current thread is a BotOwn thread (see {@link #isBotOwnThread()}, and if so, returns the {@link Worker} Thread.
     *
     * @param customMessage A custom message to show in the possible exception.
     * @return The worker thread.
     * @throws IllegalThreadException If the thread is not a Bot-Own thread.
     */
    static Worker requireBotOwnThread(String customMessage) {
        Thread thread = Thread.currentThread();
        if (thread instanceof Worker) {
            return (Worker) thread;
        } else throw new IllegalThreadException(customMessage);
    }
    
    /**
     * Gets the Discord object attached to the current Thread, if the current thread is a {@link Worker} thread. Otherwise throws a {@link
     * IllegalThreadException}.
     *
     * @return The discord object.
     * @throws IllegalThreadException If the thread is not a Bot-Own thread.
     * @see #requireBotOwnThread()
     */
    public static Discord getThreadDiscord() {
        return requireBotOwnThread().getDiscord();
    }
}
