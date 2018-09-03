package de.kaleidox.crystalshard.internal.core.concurrent;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;
import de.kaleidox.util.CompletableFutureExtended;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class is the main concurrent implementation.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ThreadPool extends LinkedBlockingQueue {
    public final static Supplier<IllegalCallerException> BOT_THREAD_EXCEPTION = () ->
            new IllegalCallerException("That method may only be called from a bot-own " +
                    "thread, such as in listeners or scheduler tasks. You may not use it from contexts like " +
                    "CompletableFuture#thenAcceptAsync or such. User ThreadPool#isBotOwnThread to check if the current " +
                    "Thread belongs to the Bot.");
    private final static Logger logger = new Logger(ThreadPool.class);
    private final ConcurrentHashMap<Worker, AtomicBoolean> threads;
    private final DiscordInternal discord;
    private final int maxSize;
    private final Factory factory;
    private final LinkedBlockingQueue<Runnable> queue;
    private Executor executor;
    private ScheduledExecutorService scheduler;
    private String name;

    /**
     * Creates a new, unlimited ThreadPool for the specified discord object.
     *
     * @param discord The discord object to attach to this ThreadPool.
     */
    public ThreadPool(Discord discord) {
        this(discord, -1, "CrystalShard Main");
        this.executor = new BotOwn(this);
        execute(() -> scheduler = Executors.newScheduledThreadPool(30, factory));
    }

    /**
     * Creates a new limited ThreadPool for the specified discord object.
     *
     * @param discordObject The discord object to attach to this ThreadPool.
     * @param maxSize       The maximum capacity of workers for this thread. -1 for infinite.
     * @param name          The name for this ThreadPool. New Workers will get this name attached.
     */
    public ThreadPool(Discord discordObject, int maxSize, String name) {
        this.discord = (DiscordInternal) discordObject;
        this.maxSize = maxSize;
        this.threads = new ConcurrentHashMap<>();
        this.factory = new Factory();
        this.queue = new LinkedBlockingQueue<>();
        this.name = name;

        execute(() -> logger.deeptrace("New ThreadPool created: " + name));
    }

    /**
     * Gets the executor for this ThreadPool.
     * This will be useful for CompletableFuture async methods that use an executor.
     *
     * @return The executor.
     * @see CompletableFutureExtended
     * @see CompletableFuture#supplyAsync(Supplier, Executor)
     * @see CompletableFuture#acceptEitherAsync(CompletionStage, Consumer, Executor)
     * @see CompletableFuture#applyToEitherAsync(CompletionStage, Function, Executor)
     * @see CompletableFuture#completeAsync(Supplier, Executor)
     * @see CompletableFuture#handleAsync(BiFunction, Executor)
     * @see CompletableFuture#runAfterBothAsync(CompletionStage, Runnable, Executor)
     * @see CompletableFuture#runAfterEitherAsync(CompletionStage, Runnable, Executor)
     * @see CompletableFuture#runAsync(Runnable, Executor)
     * @see CompletableFuture#thenAcceptAsync(Consumer, Executor)
     * @see CompletableFuture#thenAcceptBothAsync(CompletionStage, BiConsumer, Executor)
     * @see CompletableFuture#thenApplyAsync(Function, Executor)
     * @see CompletableFuture#thenCombineAsync(CompletionStage, BiFunction, Executor)
     * @see CompletableFuture#thenComposeAsync(Function, Executor)
     * @see CompletableFuture#thenRunAsync(Runnable, Executor)
     * @see CompletableFuture#whenCompleteAsync(BiConsumer, Executor)
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * This method is used internally to start heartbeating to discord.
     * Do not call this method on your own.
     *
     * @param heartbeat The heartbeat interval.
     */
    public void startHeartbeat(long heartbeat) {
        scheduler.scheduleAtFixedRate(() ->
                discord.getWebSocket().heartbeat(), heartbeat, heartbeat, TimeUnit.MILLISECONDS);
        scheduler.schedule(() -> {
            synchronized (discord) {
                discord.notify();
            }
        }, 2, TimeUnit.SECONDS);
    }

    /**
     * Schedules a new task.
     * If there is no limit on the ThreadPool or the limit is not hit, ensures that there is a Worker available.
     *
     * @param task The task to execute.
     */
    public void execute(Runnable task) {
        synchronized (queue) {
            if (threads.size() < maxSize || maxSize == -1) {
                factory.getOrCreateWorker(); // Ensure there is a worker available, if the limit is not hit.
            }
            queue.add(task);
            queue.notify();
        }
    }

    /**
     * Gets the ThreadFactory.
     *
     * @return The ThreadFactory.
     */
    public Factory getFactory() {
        return factory;
    }

    /**
     * Gets the Scheduler for this ThreadPool.
     * This will be useful for e.g. updating the discord status.
     *
     * @return The ScheduledExecutorService for this ThreadPool.
     */
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * Returns whether the current thread is a BotOwn thread; meaning the current thread is a {@link Worker} Thread.
     * This method is required if you statically want to get a Discord instance using {@link #getThreadDiscord()}.
     *
     * @return Whether the current thread is a bot own thread.
     */
    public static boolean isBotOwnThread() {
        return Thread.currentThread() instanceof Worker;
    }

    /**
     * Checks if the current thread is a BotOwn thread (see {@link #isBotOwnThread()}, and if so, returns the
     * {@link Worker} Thread.
     *
     * @return The worker thread.
     * @throws IllegalCallerException If the thread is not a Bot-Own thread.
     */
    public static Worker requireBotOwnThread() {
        Thread thread = Thread.currentThread();
        if (thread instanceof Worker) {
            return (Worker) thread;
        } else throw BOT_THREAD_EXCEPTION.get();
    }

    /**
     * Checks if the current thread is a BotOwn thread (see {@link #isBotOwnThread()}, and if so, returns the
     * {@link Worker} Thread.
     *
     * @param customMessage A custom message to show in the possible exception.
     * @return The worker thread.
     * @throws IllegalCallerException If the thread is not a Bot-Own thread.
     */
    public static Worker requireBotOwnThread(String customMessage) {
        Thread thread = Thread.currentThread();
        if (thread instanceof Worker) {
            return (Worker) thread;
        } else throw new IllegalCallerException(customMessage);
    }

    /**
     * Gets the Discord object attached to the current Thread, if the current thread is a {@link Worker} thread.
     * Otherwise throws a {@link IllegalCallerException}.
     *
     * @return The discord object.
     * @throws IllegalCallerException If the thread is not a Bot-Own thread.
     * @see #requireBotOwnThread()
     */
    public static Discord getThreadDiscord() {
        requireBotOwnThread();
        return ((Worker) Thread.currentThread()).getDiscord();
    }

    /**
     * This class represents the Factory for new {@link Worker} threads.
     */
    public class Factory implements ThreadFactory {
        final AtomicInteger nameCounter = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            synchronized (queue) {
                Worker worker = getOrCreateWorker();
                queue.add(r);
                queue.notify();
                return worker;
            }
        }

        /**
         * Checks if an older {@link Worker} threads is available, otherwise creates a new Worker thread and returns it.
         *
         * @return A {@link Worker} thread.
         */
        public Worker getOrCreateWorker() {
            return threads.entrySet()
                    .stream()
                    .filter(entry -> !entry.getValue().get())
                    .findFirst()
                    .map(Map.Entry::getKey)
                    .orElseGet(() -> {
                        Worker worker = new Worker(discord, nameCounter.getAndIncrement());
                        AtomicBoolean marker = new AtomicBoolean(false);
                        threads.put(worker, marker);
                        worker.setMarker(marker);
                        logger.deeptrace("New worker created: " + worker.toString());
                        if (!worker.isAlive()) {
                            worker.start();
                            logger.deeptrace("Worker Thread \"" + worker.getName() + "\" started!");
                        }
                        return worker;
                    });
        }
    }

    /**
     * This class represents a bot-own worker thread.
     */
    public class Worker extends Thread {
        private final DiscordInternal discord;
        private AtomicBoolean marker;

        /**
         * Creates a new {@link Worker} thread.
         *
         * @param discord The discord object to attach this worker to.
         * @param id      The id of this worker thread. If the pool's maximum size is {@code 1}, this gets ignored.
         */
        Worker(DiscordInternal discord, int id) {
            super(name == null ? ("Worker Thread #" + id) : name + " Thread" + (maxSize == 1 ? "" : " #" + id));
            this.discord = discord;
            this.marker = new AtomicBoolean(false);
        }

        /**
         * Gets the marker that marks this thread as busy.
         * Marker is {@code TRUE} if the worker is busy.
         * Marker is {@code FALSE} if the worker is busy.
         *
         * @return The marker.
         */
        public AtomicBoolean getMarker() {
            return marker;
        }

        private void setMarker(AtomicBoolean marker) {
            this.marker = marker;
        }

        /**
         * Gets the discord instance attached to the current worker thread.
         *
         * @return The discord object.
         * @see #getThreadDiscord()
         */
        public DiscordInternal getDiscord() {
            return discord;
        }

        @Override
        public void run() {
            Runnable task;
            //noinspection InfiniteLoopStatement
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            marker.set(false);
                            queue.wait();
                        } catch (InterruptedException e) {
                            logger.exception(e);
                        }
                    }
                    marker.set(true);
                    task = queue.poll();
                }

                try {
                    assert task != null;
                    task.run();
                } catch (Throwable e) {
                    logger.exception(e);
                }
            }
        }
    }
}
