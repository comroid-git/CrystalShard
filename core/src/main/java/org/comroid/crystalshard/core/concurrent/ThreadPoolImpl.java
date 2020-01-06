package org.comroid.crystalshard.core.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.api.Discord;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.NotNull;

import static org.comroid.crystalshard.core.concurrent.WorkerThreadImpl.Status.BUSY;
import static org.comroid.crystalshard.util.ThreadpoolArrayUtil.allNull;
import static org.comroid.crystalshard.util.ThreadpoolArrayUtil.getAny;
import static org.comroid.crystalshard.util.ThreadpoolArrayUtil.notNullSum;

public class ThreadPoolImpl implements ThreadPool {
    static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final Discord api;
    private final Thread taskAssigner;
    private final Queue<Runnable> queue = new LinkedBlockingQueue<>();
    final String name;
    final int concurrencyLimit;
    final ThreadGroup threadGroup;

    /*
    Workermatrix
    Workers are held here.
    
    The first layer is the state of all workers inside the matrix.
    The second layer can be any worker at that state.
     - workerMap[0] -> Idle workers
     - workerMap[1] -> Busy workers
     
     Idle workers are consumed from top-to-bottom.
     If no worker is ready and busyWorkers.length 
     
     Workers will be moved in-between layers based on whether they are idle or not.
     */
    final WorkerThreadImpl[][] workerMap;
    final WorkerThreadImpl[] idleWorkers;
    final WorkerThreadImpl[] busyWorkers;

    final ScheduledExecutorService scheduledExecutorService;

    private boolean shutdown = false;

    int workerCounter = 0;

    public ThreadPoolImpl(Discord api, String name, int concurrencyLimit) {
        this.api = api;
        this.name = name;
        this.concurrencyLimit = concurrencyLimit;
        this.threadGroup = new ThreadGroup(CrystalShard.THREAD_GROUP, String.format("%s-%s", CrystalShard.THREAD_GROUP.getName(), name));

        workerMap = new WorkerThreadImpl[2][concurrencyLimit];
        idleWorkers = workerMap[0];
        busyWorkers = workerMap[1];

        this.taskAssigner = new Thread(threadGroup, () -> {
            do {
                try {
                    synchronized (queue) {
                        while (queue.peek() == null)
                            queue.wait(10);

                        WorkerThreadImpl worker = null;

                        while (worker == null) {
                            // if no idle workers and less total workers than concurrencyLimit
                            // create new worker
                            if (allNull(idleWorkers) && notNullSum(idleWorkers, busyWorkers) < concurrencyLimit) {
                                logger.at(Level.FINER).log("Creating new Worker in pool %s", ThreadPoolImpl.this.toString());
                                worker = new WorkerThreadImpl(api, ThreadPoolImpl.this, workerCounter++);
                            } else if (!allNull(idleWorkers) && notNullSum(idleWorkers, busyWorkers) == concurrencyLimit) {
                                logger.at(Level.FINE)
                                        .atMostEvery(10, TimeUnit.SECONDS)
                                        .log("All workers are busy! Is this correct?");
                                queue.wait(1000 /* if all workers are busy, this will take a while */);
                            } else worker = getAny(idleWorkers);
                        }

                        if (worker.changeStatus(WorkerThreadImpl.Status.BUSY))
                            worker.takeLoad(queue.poll());
                    }
                } catch (InterruptedException e) {
                    ThreadPoolImpl.logger.at(Level.SEVERE)
                            .withCause(e)
                            .log();
                }
            } while (true);
        }, threadGroup.getName() + "-TaskAssigner");
        this.taskAssigner.start();

        this.scheduledExecutorService = Executors.newScheduledThreadPool(concurrencyLimit, this);
    }

    @Override
    public Thread newThread(@NotNull Runnable task) {
        if (shutdown)
            throw new IllegalStateException("ThreadPool was shut down!");

        // first try to find existing worker
        for (WorkerThreadImpl idleWorker : idleWorkers) {
            if (idleWorker != null) {
                if (idleWorker.changeStatus(BUSY)) {
                    idleWorker.takeLoad(task);
                    return idleWorker.internalThread;
                }
            }
        }

        // if didn't work, queue the task
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }

        // no idle thread could be created here
        return null;
    }

    @Override
    public ScheduledExecutorService getUnderlyingScheduler() {
        return scheduledExecutorService;
    }

    @Override
    public @NotNull ScheduledFuture<?> schedule(@NotNull Runnable task, long delay, @NotNull TimeUnit unit) {
        return scheduledExecutorService.schedule(task, delay, unit);
    }

    @Override
    public @NotNull <V> ScheduledFuture<V> schedule(@NotNull Callable<V> task, long delay, @NotNull TimeUnit unit) {
        return scheduledExecutorService.schedule(task, delay, unit);
    }

    @Override
    public @NotNull ScheduledFuture<?> scheduleAtFixedRate(@NotNull Runnable task, long initialDelay, long period, @NotNull TimeUnit unit) {
        return scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    @Override
    public @NotNull ScheduledFuture<?> scheduleWithFixedDelay(@NotNull Runnable task, long initialDelay, long delay, @NotNull TimeUnit unit) {
        return scheduledExecutorService.scheduleWithFixedDelay(task, initialDelay, delay, unit);
    }

    @Override
    public void shutdown() {
        shutdown = true;

        queue.clear();
    }

    @Override
    public @NotNull List<Runnable> shutdownNow() {
        return new ArrayList<>() {{
            addAll(queue);
            shutdown();
        }};
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public boolean isTerminated() {
        return isShutdown() && busyWorkers.length == 0;
    }

    @Override
    public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) {
        return false; // todo
    }

    @Override
    public @NotNull <T> Future<T> submit(@NotNull final Callable<T> task) {
        final CompletableFuture<T> future = new CompletableFuture<>();

        execute(() -> {
            try {
                future.complete(task.call());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Override
    public @NotNull <T> Future<T> submit(@NotNull final Runnable task, final T result) {
        return CompletableFuture.supplyAsync(() -> {
            task.run();
            return result;
        }, this);
    }

    @Override
    public @NotNull Future<?> submit(@NotNull final Runnable task) {
        return CompletableFuture.supplyAsync(() -> {
            task.run();
            return null;
        }, this);
    }

    @Override
    public @NotNull <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) {
        return tasks.stream()
                .sequential()
                .map(this::submit)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks,
                                                  long timeout, @NotNull TimeUnit unit) {
        return invokeAll(tasks); // todo
    }

    @Override
    public @NotNull <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) {
        return null; // todo
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks,
                           long timeout, @NotNull TimeUnit unit) {
        return null; // todo
    }

    @Override
    public void execute(@NotNull Runnable task) {
        newThread(task);
    }
}
