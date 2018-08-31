package de.kaleidox.crystalshard.internal.core.concurrent;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

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
    private ScheduledExecutorService scheduler;
    private String name = null;

    public ThreadPool(Discord discord) {
        this.discord = (DiscordInternal) discord;
        this.threads = new ConcurrentHashMap<>();
        this.factory = new Factory();
        this.queue = new LinkedBlockingQueue<>();
        this.maxSize = -1;

        execute(() -> scheduler = Executors.newScheduledThreadPool(30, factory));
    }

    public ThreadPool(Discord discordObject, int maxSize, String name) {
        this.discord = (DiscordInternal) discordObject;
        this.maxSize = maxSize;
        this.threads = new ConcurrentHashMap<>();
        this.factory = new Factory();
        this.queue = new LinkedBlockingQueue<>();
        this.name = name;

        execute(() -> logger.trace("New ThreadPool created: " + name));
    }

    public void startHeartbeat(long heartbeat) {
        scheduler.scheduleAtFixedRate(() ->
                discord.getWebSocket().heartbeat(), heartbeat, heartbeat, TimeUnit.MILLISECONDS);
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            if (threads.size() < maxSize || maxSize == -1) {
                threads.entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue().get())
                        .findFirst()
                        .map(Map.Entry::getKey)
                        .orElseGet(() -> {
                            Worker worker = new Worker(discord, factory.nameCounter.getAndIncrement());
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
            queue.add(task);
            queue.notify();
        }
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public static boolean isBotOwnThread() {
        return Thread.currentThread() instanceof Worker;
    }

    public static boolean requireBotOwnThread() {
        Thread thread = Thread.currentThread();
        if (thread instanceof Worker) {
            return true;
        } else throw BOT_THREAD_EXCEPTION.get();
    }

    private class Factory implements ThreadFactory {
        final AtomicInteger nameCounter = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            synchronized (queue) {
                Worker worker = new Worker(discord, nameCounter.getAndIncrement());
                queue.add(r);
                queue.notify();
                return worker;
            }
        }
    }

    public class Worker extends Thread {
        private final DiscordInternal discord;
        private AtomicBoolean marker;

        Worker(DiscordInternal discord, int id) {
            super(name == null ? ("Worker Thread #" + id) : name + " Thread #" + id);
            this.discord = discord;
            this.marker = new AtomicBoolean(false);
        }

        public AtomicBoolean getMarker() {
            return marker;
        }

        public void setMarker(AtomicBoolean marker) {
            this.marker = marker;
        }

        public DiscordInternal getDiscord() {
            return discord;
        }

        @Override
        public void run() {
            Runnable task;
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
