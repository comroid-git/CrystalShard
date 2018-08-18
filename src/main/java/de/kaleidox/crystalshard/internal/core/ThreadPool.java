package de.kaleidox.crystalshard.internal.core;

import de.kaleidox.logging.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool extends LinkedBlockingQueue {
    private final static Logger logger = new Logger(ThreadPool.class);
    private final int nThreads;
    private final Worker[] threads;
    private final LinkedBlockingQueue<Runnable> queue;

    public ThreadPool(int threads) {
        this.nThreads = threads;
        this.queue = new LinkedBlockingQueue<>();
        this.threads = new Worker[nThreads];

        for (int i = 0; i < nThreads; i++) {
            this.threads[i] = new Worker();
            this.threads[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    private class Worker extends Thread {
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
