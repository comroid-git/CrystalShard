package de.kaleidox.crystalshard.core.concurrent;

import de.kaleidox.crystalshard.core.net.socket.WebSocketClientImpl;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * This class is the main concurrent implementation.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ThreadPoolImpl implements de.kaleidox.crystalshard.core.concurrent.ThreadPool {
    // Static Fields
    private final static Logger logger = new Logger(ThreadPoolImpl.class);
    private final ConcurrentHashMap<Worker, AtomicBoolean> threads;
    private final Discord discord;
    private final int maxSize;
    private final LinkedBlockingQueue<Task> queue;
    private final AtomicInteger busyThreads = new AtomicInteger(0);
    private final Factory factory;
    private Executor executor;
    private ScheduledExecutorService scheduler;
    private String name;
    private List<Worker> factoriedThreads = new ArrayList<>();

    /**
     * Creates a new, unlimited ThreadPool for the specified discord object.
     *
     * @param discord The discord object to attach to this ThreadPool.
     */
    public ThreadPoolImpl(Discord discord) {
        this(discord, -1, "CrystalShard Main Worker");
        this.executor = new BotOwn(this);
        this.scheduler = Executors.newSingleThreadScheduledExecutor(factory);

        scheduler.scheduleAtFixedRate(this::cleanupThreads, 30, 30, TimeUnit.SECONDS);
    }

    /**
     * Creates a new limited ThreadPool for the specified discord object.
     *
     * @param discordObject The discord object to attach to this ThreadPool.
     * @param maxSize       The maximum capacity of workers for this thread. -1 for infinite.
     * @param name          The name for this ThreadPool. New Workers will get this name attached.
     */
    public ThreadPoolImpl(Discord discordObject, int maxSize, String name) {
        this.discord = discordObject;
        this.maxSize = maxSize;
        this.threads = new ConcurrentHashMap<>();
        this.queue = new LinkedBlockingQueue<>();
        this.name = name;
        this.factory = new Factory();

        execute(() -> logger.deeptrace("New ThreadPool created: " + name));
    }

    @Override
    public void execute(Runnable task, String... description) {
        synchronized (queue) {
            if (threads.size() < maxSize || maxSize == -1) {
                if (busyThreads.get() <= queue.size())
                    factory.getOrCreateWorker(); // Ensure there is a worker available, if the limit is not hit.
            }
            queue.add(new Task(task, description));
            queue.notify();
        }
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public Discord getDiscord() {
        return discord;
    }

    /**
     * This method is used internally to start heartbeating to discord. Do not call this method on your own.
     *
     * @param heartbeat The heartbeat interval.
     */
    public void startHeartbeat(long heartbeat) {
        scheduler.scheduleAtFixedRate(() -> ((WebSocketClientImpl) discord.getWebSocket()).heartbeat(), heartbeat, heartbeat, TimeUnit.MILLISECONDS);
    }

    /**
     * Removes terminated Threads from the {@code factoriedThreads} list, and decrements the name counter for each thread.
     */
    void cleanupThreads() {
        factoriedThreads.stream()
                .filter(worker -> worker.getState() == Thread.State.TERMINATED) // only exited threads
                .peek(Worker::interrupt) // interrupt the thread
                .peek(worker -> factory.nameCounter.decrementAndGet()) // decrement the id counter by one
                // each thread
                .forEach(factoriedThreads::remove); // remove the thread from the list
    }

    /**
     * Used to exclude deeptracing of tasks that come from CompletableFuture async methods.
     *
     * @param task The task to check.
     * @return Whether the task is most likely from an async stage.
     */
    private static boolean nonFutureTask(Runnable task) {
        return !task.toString()
                .toLowerCase()
                .contains("future");
    }

    public class Factory implements ThreadFactory {
        private final AtomicInteger nameCounter = new AtomicInteger(1);

        // Override Methods
        @Override
        public Thread newThread(Runnable r) {
            Worker worker = new WorkerImpl(r, discord, nameCounter.getAndIncrement());
            factoriedThreads.add(worker);
            return worker;
        }

        /**
         * Checks if an older {@link Worker} threads is available, otherwise creates a new Worker thread and returns it.
         *
         * @return A {@link Worker} thread.
         */
        public Worker getOrCreateWorker() {
            return threads.entrySet()
                    .stream()
                    .filter(entry -> !entry.getValue()
                            .get())
                    .findFirst()
                    .map(Map.Entry::getKey)
                    .orElseGet(() -> {
                        WorkerImpl worker = new WorkerImpl(discord, nameCounter.getAndIncrement());
                        threads.put(worker, worker.isBusy);
                        //logger.deeptrace("New worker created: " + worker.getName());
                        if (!worker.isAlive()) {
                            worker.start();
                            //logger.deeptrace("Worker Thread \"" + worker.getName() + "\" started!");
                        }
                        return worker;
                    });
        }
    }

    /**
     * This class represents an implementation of an executor interface to this current ThreadPool.
     */
    public class BotOwn implements Executor {
        private final ThreadPoolImpl pool;

        /**
         * Creates a new executor instance.
         *
         * @param pool The thread pool to execute in.
         */
        BotOwn(ThreadPoolImpl pool) {
            this.pool = pool;
        }

        // Override Methods
        @Override
        public void execute(Runnable command) {
            pool.execute(command);
        }
    }

    /**
     * This class represents a bot-own worker thread.
     */
    public class WorkerImpl extends Worker {
        private final Discord discord;
        private final AtomicMarkableReference<Task> nextTask = new AtomicMarkableReference<>(null, false);
        private final AtomicBoolean isBusy;
        private final boolean runnableAttachedThread;

        /**
         * Creates a new {@link Worker} thread.
         *
         * @param discord The discord object to attach this worker to.
         * @param id      The id of this worker thread. If the pool's maximum size is {@code 1}, this gets ignored.
         */
        WorkerImpl(Discord discord, int id) {
            super(name == null ? ("Worker Thread #" + id) : name + " Thread" + (maxSize == 1 ? "" : " #" + id));
            this.discord = discord;
            this.isBusy = new AtomicBoolean(false);
            this.runnableAttachedThread = false;
        }

        WorkerImpl(Runnable initTask, Discord discord, int id) {
            super(initTask, name == null ? ("Worker Thread #" + id) : name + " Thread" + (maxSize == 1 ? "" : " #" + id));
            this.discord = discord;
            this.isBusy = new AtomicBoolean(true);
            this.runnableAttachedThread = true;
        }

        // Override Methods
        @Override
        public void run() {
            if (!runnableAttachedThread) {
                Task task = null;
                //noinspection InfiniteLoopStatement
                while (true) {
                    synchronized (queue) {
                        try {
                            while (queue.isEmpty() && !nextTask.isMarked()) {
                                try {
                                    queue.wait();
                                } catch (InterruptedException e) {
                                    logger.exception(e);
                                }
                            }
                            task = nextTask.isMarked() ? nextTask.getReference() : queue.poll();
                            assert task != null;
                            busy();
                            /*if (nonFutureTask(task))
                                logger.deeptrace("Running " + (nextTask.isMarked() ? "attached" : "scheduled") +
                                        " task #" + task.hashCode() + (task.hasDescription() ?
                                        " with description: " + task.getDescription() : ""));*/
                            task.run();
                            /*if (nonFutureTask(task))
                                logger.deeptrace((nextTask.isMarked() ? "Attached" : "Scheduled") +
                                        " task #" + task.hashCode() + " finished.");*/
                            unbusy();
                            if (nextTask.isMarked()) nextTask.set(null, false); // if nextTask is set, unset it, because
                            // its being run
                        } catch (Throwable e) {
                            assert task != null;
                            /*if (nonFutureTask(task))
                                logger.exception(e, (nextTask.isMarked() ? "Attached" : "Scheduled") +
                                        " task " + ("#" + task.hashCode()) + " finished with an exception:");*/
                        }
                    }
                }
            } else {
                super.run();
            }
        }

        private void busy() {
            isBusy.set(true);
            busyThreads.incrementAndGet();
        }

        private void unbusy() {
            isBusy.set(false);
            busyThreads.decrementAndGet();
        }

        /**
         * Attaches a new Runnable to this worker. This method should not be used for chaining tasks, but for attaching runnables to worker threads in the
         * {@code java.lang.Thread.State.RUNNABLE} state. For chaining tasks, use {@link #execute(Runnable, String...)} instead.
         *
         * @param task The task to attach.
         */
        void attachTask(Task task) {
            synchronized (queue) {
                //noinspection StatementWithEmptyBody
                if (isBusy.get()) {
                    execute(task); // if current worker is busy, add task to the queue
                } else {
                    this.nextTask.set(task, true);
                    queue.notify();
                }
            }
        }

        /**
         * Gets the discord instance attached to the current worker thread.
         *
         * @return The discord object.
         * @see #getThreadDiscord()
         */
        public Discord getDiscord() {
            return discord;
        }
    }

    private class Task implements Runnable {
        private final Runnable runnable;
        private final String[] description;

        Task(Runnable runnable, String... description) {
            this.runnable = runnable;
            this.description = description;
        }

        // Override Methods
        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Exception e) {
                logger.exception(e);
            }
        }

        public boolean hasDescription() {
            return description.length != 0;
        }

        public String getDescription() {
            return description.length == 0 ? "No task description." : String.join(" ", description);
        }
    }
}
