package de.kaleidox.crystalshard.internal.core.concurrent;

import de.kaleidox.crystalshard.internal.DiscordInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.logging.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool extends LinkedBlockingQueue {
    private final static Logger logger = new Logger(ThreadPool.class);
    private final int nThreads;
    private final Worker[] threads;
    private final LinkedBlockingQueue<Runnable> queue;
    private final DiscordInternal discord;
    private Scheduler scheduler;

    public ThreadPool(Discord discord, int threads) {
        this.discord = (DiscordInternal) discord;
        this.nThreads = threads;
        this.queue = new LinkedBlockingQueue<>();
        this.threads = new Worker[nThreads];

        for (int i = 0; i < nThreads; i++) {
            this.threads[i] = new Worker(this.discord);
            this.threads[i].start();
        }
    }

    public void initScheduler(long heartbeat) {
        this.scheduler = new Scheduler(discord, heartbeat);
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public class Worker extends Thread {
        private final DiscordInternal discord;

        Worker(DiscordInternal discord) {
            this.discord = discord;
        }

        public DiscordInternal getDiscord() {
            return discord;
        }

        public void run() {
            Runnable task;
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            logger.exception(e);
                        }
                    }
                    task = queue.poll();
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    logger.exception(e);
                }
            }
        }
    }
}
