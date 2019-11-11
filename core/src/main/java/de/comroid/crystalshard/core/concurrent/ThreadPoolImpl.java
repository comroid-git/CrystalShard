package de.comroid.crystalshard.core.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import de.comroid.crystalshard.CrystalShard;
import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.util.ThreadpoolArrayUtil;

import com.google.common.flogger.FluentLogger;
import org.jetbrains.annotations.NotNull;

import static de.comroid.crystalshard.util.ThreadpoolArrayUtil.allNull;
import static de.comroid.crystalshard.util.ThreadpoolArrayUtil.notNullSum;

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

    int workerCounter = 0;

    public ThreadPoolImpl(Discord api, String name, int concurrencyLimit) {
        this.api = api;
        this.name = name;
        this.concurrencyLimit = concurrencyLimit;
        this.threadGroup = new ThreadGroup(CrystalShard.THREAD_GROUP, String.format("%s-%s", CrystalShard.THREAD_GROUP.getName(), name));

        workerMap = new WorkerThreadImpl[2][concurrencyLimit];
        idleWorkers = workerMap[0];
        busyWorkers = workerMap[1];

        this.taskAssigner = new Thread(threadGroup, new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        synchronized (queue) {
                            while (queue.peek() == null)
                                queue.wait(10);

                            WorkerThreadImpl worker = null;

                            while (worker == null) {
                                // if no idle workers and less total workers than concurrencyLimit
                                // create new worker
                                if (allNull(idleWorkers) && notNullSum(idleWorkers, busyWorkers) < concurrencyLimit)
                                    worker = new WorkerThreadImpl(api, ThreadPoolImpl.this, workerCounter++);
                                else if (!allNull(idleWorkers) && notNullSum(idleWorkers, busyWorkers) >= concurrencyLimit) {
                                    worker = getAny(idleWorkers)
                                }
                            }

                            if (worker)
                        }
                    } catch (InterruptedException e) {
                        ThreadPoolImpl.logger.at(Level.SEVERE)
                                .withCause(e)
                                .log();
                    }
                } while (true);
            }
        }, threadGroup.getName() + "-TaskAssigner");
        this.taskAssigner.start();

        this.scheduledExecutorService = Executors.newScheduledThreadPool(concurrencyLimit, this);
    }

    @Override
    public Thread newThread(@NotNull Runnable task) {
        // first try to find existing worker
        for (WorkerThreadImpl idleWorker : idleWorkers) {
            if (idleWorker != null) {
                if (idleWorker.changeStatus(WorkerThreadImpl.Status.BUSY)) {
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
    public ScheduledExecutorService getScheduler() {
        return scheduledExecutorService;
    }

    private WorkerThreadImpl findFreeWorker$blocking() {
        return null;
    }

    @Override public void shutdown() {

    }

    @NotNull @Override public List<Runnable> shutdownNow() {
        return null;
    }

    @Override public boolean isShutdown() {
        return false;
    }

    @Override public boolean isTerminated() {
        return false;
    }

    @Override public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return false;
    }

    @NotNull @Override public <T> Future<T> submit(@NotNull Callable<T> task) {
        return null;
    }

    @NotNull @Override public <T> Future<T> submit(@NotNull Runnable task, T result) {
        return null;
    }

    @NotNull @Override public Future<?> submit(@NotNull Runnable task) {
        return null;
    }

    @NotNull @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @NotNull @Override
    public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return null;
    }

    @NotNull @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override public void execute(@NotNull Runnable command) {

    }
}
