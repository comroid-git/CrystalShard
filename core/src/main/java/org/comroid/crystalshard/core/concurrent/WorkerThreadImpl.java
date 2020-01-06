package org.comroid.crystalshard.core.concurrent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import org.comroid.crystalshard.api.Discord;

import org.intellij.lang.annotations.MagicConstant;

import static org.comroid.crystalshard.util.ThreadpoolArrayUtil.add;
import static org.comroid.crystalshard.util.ThreadpoolArrayUtil.remove;

public class WorkerThreadImpl implements WorkerThread {
    private final Discord api;
    private final ThreadPoolImpl threadPool;
    final Thread internalThread;
    
    private final AtomicReference<Runnable> task = new AtomicReference<>(null);

    final int id;

    private @MagicConstant(flagsFromClass = Status.class) int status;

    public WorkerThreadImpl(Discord api, ThreadPoolImpl threadPool, int id) {
        this.api = api;
        this.threadPool = threadPool;
        this.id = id;

        internalThread = new Thread(threadPool.threadGroup, new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        synchronized (task) {
                            Runnable runnable;

                            while ((runnable = task.get()) == null)
                                task.wait(200);
                            
                            runnable.run();
                            task.set(null);
                            changeStatus(Status.IDLE);
                        }
                    } catch (InterruptedException e) {
                        ThreadPoolImpl.logger.at(Level.SEVERE)
                                .withCause(e)
                                .log();
                    }
                } while (true);
            }
        }, String.format("%s-Worker#%d", threadPool.threadGroup.getName(), id));
        internalThread.start();

        add(this, threadPool.idleWorkers);

        status = Status.IDLE;
    }

    @Override
    public Discord getAPI() {
        return api;
    }

    @Override
    public String toString() {
        return String.format("WorkerThread #%d - [pool:%s,api:%s]", id, threadPool, api);
    }

    boolean changeStatus(@MagicConstant(flagsFromClass = Status.class) int status) {
        if (threadPool.workerMap[status].length < threadPool.concurrencyLimit) {
            final WorkerThreadImpl[] oldPlace = threadPool.workerMap[this.status];

            int r;
            if ((r = remove(this, oldPlace)) != -1)
                threadPool.workerMap[status][r] = this;
            else throw new IllegalStateException("Could not find Worker in opposite state map!");
        } else return false;

        this.status = status;
        return true;
    }

    void takeLoad(Runnable task) throws IllegalThreadStateException {
        synchronized (this.task) {
            if (task != null)
                throw new IllegalThreadStateException("Worker already has a task assigned!");

            this.task.set(Objects.requireNonNull(task, "Task cannot be null!"));
            this.task.notify();
        }
    }

    static class Status {
        static final int IDLE = 0;
        static final int BUSY = 1;
    }
}
